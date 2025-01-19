package com.ssblur.scriptor.helpers

import com.ssblur.scriptor.advancement.ScriptorAdvancements
import com.ssblur.scriptor.config.ScriptorConfig
import com.ssblur.scriptor.data.saved_data.DictionarySavedData.Companion.computeIfAbsent
import com.ssblur.scriptor.effect.EmpoweredStatusEffect
import com.ssblur.scriptor.helpers.LimitedBookSerializer.decodeText
import com.ssblur.scriptor.helpers.targetable.SpellbookTargetable
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

object SpellbookHelper {
    var SPELLBOOKS: List<Item> = ArrayList()

    fun castFromItem(itemStack: ItemStack, player: Player): Boolean {
        val text = itemStack.get(DataComponents.WRITTEN_BOOK_CONTENT)
        val level = player.level()
        if (text == null || level !is ServerLevel) return false

        level.playSound(
            null,
            player.blockPosition(),
            SoundEvents.EVOKER_CAST_SPELL,
            SoundSource.PLAYERS,
            0.4f,
            level.getRandom().nextFloat() * 1.2f + 0.6f
        )

        val spell = computeIfAbsent(level).parse(decodeText(text))
        if (spell != null) {
            if (spell.cost() > ScriptorConfig.TOME_MAX_COST()) {
                player.sendSystemMessage(Component.translatable("extra.scriptor.fizzle"))
                ScriptorAdvancements.FIZZLE.get().trigger(player as ServerPlayer)
                if (!player.isCreative()) addCooldown(
                    player,
                    Math.round(350.0 * ScriptorConfig.TOME_COOLDOWN_MULTIPLIER().toDouble() / 100.0).toInt()
                )
                return true
            }
            spell.cast(SpellbookTargetable(itemStack, player, player.inventory.selected).withTargetItem(false))
            if (!player.isCreative) {
                var costScale = 1.0
                for (instance in player.activeEffects)
                    if (instance.effect.value() is EmpoweredStatusEffect)
                        for (i in 0..instance.amplifier) costScale *= (instance.effect.value() as EmpoweredStatusEffect).scale.toDouble()
                val adjustedCost =
                    costScale * spell.cost() * (ScriptorConfig.TOME_COOLDOWN_MULTIPLIER().toDouble() / 100.0)
                addCooldown(player, Math.round(adjustedCost * 7).toInt())
            }
            return false
        }
        return true
    }

    fun addCooldown(player: Player, time: Int) {
        for (spellbook in SPELLBOOKS) player.cooldowns.addCooldown(spellbook, time)
    }
}
