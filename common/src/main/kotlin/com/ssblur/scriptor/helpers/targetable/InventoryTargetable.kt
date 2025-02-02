package com.ssblur.scriptor.helpers.targetable

import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import java.util.function.Predicate

interface InventoryTargetable {
  val container: Container?

  var targetedSlot: Int

  fun shouldIgnoreTargetedSlot(): Boolean {
    return targetedSlot == -1 && container != null
  }

  fun useFirstMatchingSlot(predicate: Predicate<ItemStack>) {
    val slot = getFirstMatchingSlot(predicate)
    if (slot >= 0) targetedSlot = slot
  }

  fun getFirstMatchingSlot(predicate: Predicate<ItemStack>): Int {
    if (container != null) for (i in 0 until container!!.containerSize) if (predicate.test(container!!.getItem(i))) {
      return i
    }
    return -1
  }

  val firstFilledSlot: Int
    get() = getFirstMatchingSlot { item: ItemStack -> !item.isEmpty }

  fun getFirstMatchingSlot(itemStack: ItemStack): Int {
    return getFirstMatchingSlot { item: ItemStack ->
      (item.isEmpty
          || (ItemStack.isSameItemSameComponents(item, itemStack)
          && (item.count + itemStack.count) <= item.maxStackSize
          ))
    }
  }

  fun getFirstMatchingSlotNotEmpty(itemStack: ItemStack): Int {
    return getFirstMatchingSlot { item: ItemStack ->
      (ItemStack.isSameItemSameComponents(item, itemStack)
          && (item.count + itemStack.count) <= item.maxStackSize)
    }
  }

  fun useFirstFilledSlot() {
    val slot = firstFilledSlot
    if (slot >= 0) targetedSlot = slot
  }
}
