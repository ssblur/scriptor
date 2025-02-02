package com.ssblur.scriptor.word.action

import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.color.CustomColors.getColor
import com.ssblur.scriptor.color.interfaces.Colorable
import com.ssblur.scriptor.color.interfaces.ColorableBlock
import com.ssblur.scriptor.color.interfaces.ColorableItem
import com.ssblur.scriptor.helpers.ItemTargetableHelper
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.registry.colorable.ColorableBlockRegistry.get
import com.ssblur.scriptor.registry.colorable.ColorableBlockRegistry.has
import net.minecraft.world.item.ItemStack

class ColorAction: Action() {
  override fun apply(caster: Targetable, targetable: Targetable, descriptors: Array<Descriptor>) {
    val color = getColor(descriptors)
    if (targetable is EntityTargetable) {
      if (targetable.targetEntity is Colorable) {
        (targetable.targetEntity as Colorable).setColor(color)
      }
    }

    val itemTarget = ItemTargetableHelper.getTargetItemStack(targetable)
    if (!itemTarget.isEmpty) {
      if (itemTarget.item is ColorableItem) {
        val itemOut: ItemStack = (itemTarget.item as ColorableItem).setColor(color, itemTarget)!!
        if (!itemOut.isEmpty) {
          itemTarget.count = 0
          ItemTargetableHelper.depositItemStack(targetable, itemOut)
        }
      }
      return
    }

    val blockEntity = targetable.level.getBlockEntity(
      targetable.offsetBlockPos
    )
    if (blockEntity is Colorable) {
      blockEntity.setColor(color)
      return
    }

    val block = targetable.level.getBlockState(targetable.offsetBlockPos).block
    if (block is ColorableBlock) {
      block.setColor(color, targetable.level, targetable.offsetBlockPos)
    } else if (has(block)) {
      get(block)!!.setColor(color, targetable.level, targetable.offsetBlockPos)
    }
  }

  override fun cost() = Cost(8.0, COSTTYPE.ADDITIVE)
}
