package com.ssblur.scriptor.helpers.targetable

import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.item.ItemEntity

open class ItemEntityTargetable(targetEntity: ItemEntity): EntityTargetable(targetEntity), InventoryTargetable {
  override val container: Container?
    get() = SimpleContainer((targetEntity as ItemEntity).item)
  override var targetedSlot: Int
    get() = 0
    set(value) {}
}
