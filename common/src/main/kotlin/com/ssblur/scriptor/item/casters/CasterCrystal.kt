package com.ssblur.scriptor.item.casters

import com.ssblur.scriptor.helpers.targetable.Targetable
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

abstract class CasterCrystal(properties: Properties) : Item(properties) {
    abstract fun getTargetables(itemStack: ItemStack?, level: Level?): List<Targetable?>?
}
