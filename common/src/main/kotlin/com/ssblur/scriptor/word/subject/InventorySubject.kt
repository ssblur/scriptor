package com.ssblur.scriptor.word.subject

import com.ssblur.scriptor.word.Spell
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

interface InventorySubject {
    fun castOnItem(spell: Spell, player: Player, slot: ItemStack)
}
