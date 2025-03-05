package com.ssblur.scriptor.helpers

import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

object PlayerItemHelper {
    fun Player.addOrDropItem(itemStack: ItemStack) {
        if(!itemStack.isEmpty && !this.addItem(itemStack))
            level().addFreshEntity(ItemEntity(level(), x, y, z, itemStack))
    }
}