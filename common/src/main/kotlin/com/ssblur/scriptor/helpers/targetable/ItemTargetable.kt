package com.ssblur.scriptor.helpers.targetable

import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

open class ItemTargetable(var targetItem: ItemStack, entity: Player?): EntityTargetable(
  entity!!
) {
  var targetItemByDefault: Boolean = true

  fun shouldTargetItem(): Boolean {
    return targetItemByDefault
  }

  fun withTargetItem(targetItemByDefault: Boolean): ItemTargetable {
    this.targetItemByDefault = targetItemByDefault
    return this
  }
}
