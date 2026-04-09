package com.ssblur.scriptor.events

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.data.components.ScriptorDataComponents.EXPIRES
import com.ssblur.scriptor.extension.EntityCastCooldownExtension.mana
import com.ssblur.scriptor.helpers.ScriptionaryHelper.awardNote
import com.ssblur.scriptor.helpers.ScriptionaryHelper.hasNote
import com.ssblur.scriptor.item.ScriptorTags
import com.ssblur.unfocused.event.common.PlayerTickEvent
import com.ssblur.unfocused.extension.ItemStackExtension.matches
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.entity.npc.Villager
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.phys.AABB

object PlayerTick {
  fun init() {
    PlayerTickEvent.Before.register {
      val items = it.inventory.items
      processInventoryItem(items[(it.level().gameTime % items.size).toInt()], it)
      for (slot in EquipmentSlot.entries) processEquipment(it.getItemBySlot(slot), it)

      if((it.level().gameTime % 40) == 4L) it.mana += 475
      if((it.level().gameTime % 40) == 7L)
        if(it.level().getNearbyEntities(
            Villager::class.java,
            TargetingConditions.forNonCombat(),
          it,
            AABB.ofSize(it.position(), 4.0, 4.0, 4.0)
        ).any())
          it.awardNote(ScriptorMod.location("oddities/nominomist"))
    }
  }

  fun processEquipment(item: ItemStack, entity: Player) {
    val level = entity.level()
    if (item.count > 0) {
      if (item.has(EXPIRES) && item.get(EXPIRES)!! <= level.gameTime) {
        item.count = 0
        level.playSound(null, entity.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS)
      }
    }
  }

  fun processInventoryItem(item: ItemStack, entity: Player) {
    if(item.count <= 0) return
    if (item.has(ScriptorDataComponents.SPELL)
      && !(item matches ScriptorTags.IGNORE_SPELL_COMPONENT))
      entity.awardNote(ScriptorMod.location("casting/item_inscription"))
    else if(item matches Items.GLOW_INK_SAC && entity.hasNote(ScriptorMod.location("tools/book_of_books")))
      entity.awardNote(ScriptorMod.location("oddities/obfuscation"))
    else if(item matches ScriptorTags.SPELLBOOKS)
      entity.awardNote(ScriptorMod.location("tools/book_of_books"))
  }
}