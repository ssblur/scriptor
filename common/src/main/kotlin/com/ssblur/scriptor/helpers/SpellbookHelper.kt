package com.ssblur.scriptor.helpers

import com.ssblur.scriptor.advancement.ScriptorAdvancements
import com.ssblur.scriptor.config.ScriptorConfig
import com.ssblur.scriptor.data.components.ConditionData
import com.ssblur.scriptor.data.components.ReagentData
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.data.saved_data.DictionarySavedData.Companion.computeIfAbsent
import com.ssblur.scriptor.effect.EmpoweredStatusEffect
import com.ssblur.scriptor.extension.EntityCastCooldownExtension.canCast
import com.ssblur.scriptor.extension.EntityCastCooldownExtension.castCooldown
import com.ssblur.scriptor.helpers.LimitedBookSerializer.decodeText
import com.ssblur.scriptor.helpers.targetable.SpellbookTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.ConditionalWord
import com.ssblur.scriptor.word.descriptor.discount.ReagentDescriptor
import com.ssblur.scriptor.word.subject.HitSubject
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import kotlin.math.roundToInt

object SpellbookHelper {
  fun isInventoryCaster(
    itemStack: ItemStack,
    level: ServerLevel
  ): Boolean {
    val text = itemStack.get(DataComponents.WRITTEN_BOOK_CONTENT) ?: return false
    val spell = computeIfAbsent(level).parse(decodeText(text))
    return (spell != null && (spell.subject?.canBeCastOnInventory() == true))
  }

  fun getReagentData(
    itemStack: ItemStack,
    level: ServerLevel
  ): ReagentData {
    val text = itemStack.get(DataComponents.WRITTEN_BOOK_CONTENT) ?: return ReagentData.NONE
    val spell = computeIfAbsent(level).parse(decodeText(text)) ?: return ReagentData.NONE
    val map = mutableMapOf<ResourceLocation, Int>()
    spell.spells.forEach {
      it.descriptors.forEach { descriptor ->
        if(descriptor is ReagentDescriptor) {
          val key = BuiltInRegistries.ITEM.getKey(descriptor.item)
          map[key] = map[key] ?: 0
          map[key] = map[key]!! + 1
        }
      }
    }
    return ReagentData(map)
  }

  fun getConditionData(
    itemStack: ItemStack,
    level: ServerLevel
  ): ConditionData {
    val text = itemStack.get(DataComponents.WRITTEN_BOOK_CONTENT) ?: return ConditionData.NONE
    val data = computeIfAbsent(level)
    val spell = data.parse(decodeText(text)) ?: return ConditionData.NONE
    val list = mutableListOf<String>()

    spell.subject.let { subject ->
      if(subject is ConditionalWord) {
        data.getKey(subject)?.replace(":", ".")?.let { condition ->
          list.add(condition)
        }
      }
    }

    spell.spells.forEach {
      it.descriptors.forEach { descriptor ->
        if(descriptor is ConditionalWord) {
          data.getKey(descriptor)?.replace(":", ".")?.let { condition ->
            list.add(condition)
          }
        }
      }

      it.action.let { action ->
        if(action is ConditionalWord) {
          data.getKey(action)?.replace(":", ".")?.let { condition ->
            list.add(condition)
          }
        }
      }
    }
    return ConditionData(list)
  }

  fun castFromItem(
    itemStack: ItemStack,
    player: Player,
    maxCost: Int? = null,
    costMultiplier: Int? = null,
    cooldownFunc: (Player, Int) -> Unit = ::addCooldown,
    targetOverride: List<Targetable>? = null,
  ): Boolean {
    val adjustedCostMultiplier = (costMultiplier ?: ScriptorConfig.TOME_COOLDOWN_MULTIPLIER()).toDouble() / 100.0
    val level = player.level()
    if (level !is ServerLevel) return false
    val spell = computeIfAbsent(level).parse(
      itemStack[DataComponents.WRITTEN_BOOK_CONTENT]?.let {
        decodeText(it)
      } ?: itemStack[ScriptorDataComponents.SPELL]
    )

    if (spell != null && spell.subject !is HitSubject) {
      spell.deduplicatedDescriptorsForSubjects()
      spell.playSound(level, player.blockPosition())
      if (!player.canCast(spell, adjustedCostMultiplier * 49)) {
        player.sendSystemMessage(Component.translatable("extra.scriptor.fizzle"))
        ScriptorAdvancements.FIZZLE.get().trigger(player as ServerPlayer)
        if (!player.isCreative)
          cooldownFunc(player, (350 * adjustedCostMultiplier).roundToInt())
        return true
      }

      val subject = SpellbookTargetable(itemStack, player, player.inventory.selected)
        .withTargetItem(false)
      if(targetOverride == null)
        spell.cast(subject)
      else
        spell.castOnTargets(subject, targetOverride, castHooks = true)

      if (!player.isCreative) {
        var costScale = 1.0
        for (instance in player.activeEffects)
          if (instance.effect.value() is EmpoweredStatusEffect)
            (0..instance.amplifier).forEach{ _ ->
              costScale *= (instance.effect.value() as EmpoweredStatusEffect).scale.toDouble()
            }
        val adjustedCost =
          costScale * spell.cost() * adjustedCostMultiplier
        cooldownFunc(player, (adjustedCost * 7).roundToInt())
        return true
      }
      return false
    }
    return false
  }

  fun addCooldown(entity: Entity, time: Int) {
    if(entity !is Player || !entity.isCreative) entity.castCooldown = time.toLong() * 7L
  }
}
