package com.ssblur.scriptor.effect

import com.ssblur.scriptor.blockentity.PhasedBlockBlockEntity
import net.minecraft.core.Vec3i
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import kotlin.math.ceil

open class PhasingStatusEffect: MobEffect {
  constructor(): super(MobEffectCategory.NEUTRAL, 0x2d0096)

  constructor(mobEffectCategory: MobEffectCategory, i: Int): super(mobEffectCategory, i)

  override fun applyEffectTick(entity: LivingEntity, amplifier: Int): Boolean {
    if (entity is Player) phase(entity, 0)
    return true
  }

  override fun shouldApplyEffectTickThisTick(i: Int, j: Int): Boolean {
    return true
  }

  companion object {
    fun phase(entity: Player, bottom: Int) {
      for (x in 1 downTo -1) for (y in ceil(entity.eyeHeight.toDouble()).toInt() downTo bottom) for (z in 1 downTo -1) PhasedBlockBlockEntity.phase(
        entity.level(), entity.blockPosition().offset(
          Vec3i(x, y, z)
        )
      )
    }
  }
}