package com.ssblur.scriptor.entity.goals

import com.google.common.collect.ImmutableList
import com.mojang.datafixers.util.Pair
import com.ssblur.scriptor.extension.EntityCastCooldownExtension.castCooldown
import com.ssblur.scriptor.helpers.FakeCastHelper
import com.ssblur.scriptor.network.client.ParticleNetwork
import com.ssblur.scriptor.villagers.ScriptorVillagers
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.ai.behavior.Behavior
import net.minecraft.world.entity.ai.behavior.BehaviorControl
import net.minecraft.world.entity.ai.behavior.BehaviorUtils
import net.minecraft.world.entity.ai.memory.MemoryModuleType.*
import net.minecraft.world.entity.ai.memory.MemoryStatus.REGISTERED
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.entity.npc.Villager
import net.minecraft.world.entity.npc.VillagerProfession
import net.minecraft.world.phys.AABB

class HealVillagerGoal():
  Behavior<Villager>(mapOf(WALK_TARGET to REGISTERED, LOOK_TARGET to REGISTERED, INTERACTION_TARGET to REGISTERED)) {
  var target: Villager? = null

  override fun canStillUse(serverLevel: ServerLevel, livingEntity: Villager, l: Long): Boolean {
    return target != null
  }

  override fun checkExtraStartConditions(serverLevel: ServerLevel, livingEntity: Villager): Boolean {
    return livingEntity.villagerData.profession == ScriptorVillagers.NOMINOMIST.get()
        && livingEntity.castCooldown <= 0
        && nearbyInjuredVillagers(livingEntity).any()
  }

  override fun start(serverLevel: ServerLevel, livingEntity: Villager, l: Long) {
    target = nearbyInjuredVillagers(livingEntity).firstOrNull()
    target?.let {
      livingEntity.navigation.moveTo(it, 10.0)
    }
    super.start(serverLevel, livingEntity, l)
  }

  override fun stop(serverLevel: ServerLevel, livingEntity: Villager, l: Long) {
    target = null
    livingEntity.getBrain()?.let {
      it.eraseMemory(INTERACTION_TARGET)
      it.eraseMemory(WALK_TARGET)
      it.eraseMemory(LOOK_TARGET)
    }
    super.stop(serverLevel, livingEntity, l)
  }

  override fun tick(level: ServerLevel, villager: Villager, l: Long) {
    (target ?: return).let {
      villager.navigation.moveTo(it, 0.6)
      BehaviorUtils.lookAtEntity(villager, it)
      if(villager.distanceTo(it) < 2) {
        val spell = FakeCastHelper.NOMINOMIST_HEAL
        villager.castCooldown = (spell.cost() * 30).toLong()
        FakeCastHelper.castAs(villager, spell)
        level.playSound(null, villager.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.NEUTRAL)
        it.heal(2.0f)
        ParticleNetwork.magicScatter(level, 0x7e34bf, it.eyePosition)
        target = null
      }
    }
    super.tick(level, villager, l)
  }

  companion object {
    val DISTANCE = 32.0
    fun nearbyVillagers(villager: Villager): List<Villager> =
      villager.level().getNearbyEntities(
        Villager::class.java,
        TargetingConditions.forNonCombat(),
        villager,
        AABB.ofSize(villager.position(), DISTANCE, DISTANCE, DISTANCE)
      )

    fun nearbyInjuredVillagers(villager: Villager) =
      nearbyVillagers(villager).filter { it.health < it.maxHealth }

    fun injectBehavior(profession: VillagerProfession, list: ImmutableList<Pair<Int?, out BehaviorControl<in Villager?>?>?>?):
        ImmutableList<Pair<Int?, out BehaviorControl<in Villager?>?>?>? {
      if(list != null && profession == ScriptorVillagers.NOMINOMIST.get()) {
        return ImmutableList.copyOf(list + listOf(Pair.of(8, HealVillagerGoal())))
      }
      return list
    }
  }
}