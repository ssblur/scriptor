package com.ssblur.scriptor.helpers

import com.ssblur.scriptor.advancement.ScriptorAdvancements
import com.ssblur.scriptor.config.ScriptorConfig
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.data.saved_data.DictionarySavedData.Companion.computeIfAbsent
import com.ssblur.scriptor.effect.EmpoweredStatusEffect
import com.ssblur.scriptor.extension.EntityCastCooldownExtension.castCooldown
import com.ssblur.scriptor.helpers.LimitedBookSerializer.decodeText
import com.ssblur.scriptor.helpers.targetable.SpellbookTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.subject.HitSubject
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
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
    return (spell != null && (spell.subject.canBeCastOnInventory()))
  }

  fun castFromItem(
    itemStack: ItemStack,
    player: Player,
    maxCost: Int? = null,
    costMultiplier: Int? = null,
    cooldownFunc: (Player, Int) -> Unit = ::addCooldown,
    targetOverride: List<Targetable>? = null,
  ): Boolean {
    if(player.castCooldown > 0) return false
    val adjustedMaxCost = maxCost ?: ScriptorConfig.TOME_MAX_COST()
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
      if (spell.cost() > adjustedMaxCost) {
        player.sendSystemMessage(Component.translatable("extra.scriptor.fizzle"))
        ScriptorAdvancements.FIZZLE.get().trigger(player as ServerPlayer)
        if (!player.isCreative)
          cooldownFunc(player, (350.0 * adjustedCostMultiplier).roundToInt())
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

  fun addCooldown(player: Player, time: Int) {
    player.castCooldown = time.toLong() * 7L
  }
}
