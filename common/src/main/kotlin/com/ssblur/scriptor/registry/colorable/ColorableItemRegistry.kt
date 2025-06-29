package com.ssblur.scriptor.registry.colorable

import com.ssblur.scriptor.color.interfaces.ColorableItem
import net.minecraft.world.item.Item

object ColorableItemRegistry {
  var colorableItems: HashMap<Item, ColorableItem> = HashMap()

  var DYE_COLORABLE_ITEMS = DyeColorableItems

  fun register(item: Item, colorableItem: ColorableItem) {
    colorableItems[item] = colorableItem
  }

  fun get(item: Item): ColorableItem? {
    return colorableItems[item]
  }

  fun has(item: Item): Boolean {
    return colorableItems.containsKey(item)
  }
}
