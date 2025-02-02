package com.ssblur.scriptor.color.interfaces

import net.minecraft.world.item.ItemStack

interface ColorableItem {
  fun setColor(color: Int, itemStack: ItemStack?): ItemStack?
}
