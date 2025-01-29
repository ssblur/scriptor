package com.ssblur.scriptor.events

import com.ssblur.scriptor.data.components.ScriptorDataComponents.EXPIRES
import com.ssblur.unfocused.event.common.PlayerTickEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

object PlayerTick {
    init {
        PlayerTickEvent.Before.register{
            for (item in it.inventory.items) processItem(item, it)
            for (slot in EquipmentSlot.entries) processItem(it.getItemBySlot(slot), it)
        }
    }

    fun processItem(item: ItemStack, entity: Player) {
        val level = entity.level()
        if (item.count > 0) {
            if (item.has(EXPIRES) && item.get(EXPIRES)!! <= level.gameTime) {
                item.count = 0
                level.playSound(null, entity.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS)
            }
        }
    }
}