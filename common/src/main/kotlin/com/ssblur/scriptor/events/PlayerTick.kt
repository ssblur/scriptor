package com.ssblur.scriptor.events

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.data.components.ScriptorDataComponents.EXPIRES
import com.ssblur.scriptor.helpers.ScriptionaryHelper.awardNote
import com.ssblur.scriptor.item.ScriptorTags
import com.ssblur.unfocused.event.common.PlayerTickEvent
import com.ssblur.unfocused.extension.ItemStackExtension.matches
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

object PlayerTick {
  fun init() {
    PlayerTickEvent.Before.register {
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
      if (
        level.gameTime % 20 == 6L
        && item.has(ScriptorDataComponents.SPELL)
        && !(item matches ScriptorTags.IGNORE_SPELL_COMPONENT)
      ) {
        entity.awardNote(ScriptorMod.location("casting/item_inscription"))
      }
    }
  }
}