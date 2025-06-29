package com.ssblur.scriptor.color

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.ssblur.scriptor.color.interfaces.ColorableItem
import com.ssblur.scriptor.registry.colorable.ColorableItemRegistry.register
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import java.util.*

class DyeColorableItem: ColorableItem {
  var items: BiMap<DyeColor, Item> = HashBiMap.create()
  var random: Random = Random()

  override fun setColor(color: Int, itemStack: ItemStack?): ItemStack {
    return itemStack!!.transmuteCopy(
      items[CustomColors.getDyeColor(color, random.nextFloat(0f, Float.MAX_VALUE))]!!
    )
  }

  fun add(item: Item, dyeColor: DyeColor) {
    items[dyeColor] = item
  }

  fun register() {
    for (entry in items.values) register(entry!!, this)
  }
}
