package com.ssblur.scriptor.word.descriptor.focus.inventory

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.InventoryTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.focus.FocusDescriptor
import net.minecraft.world.item.ItemStack
import java.util.function.Predicate

class CasterFirstEmptySlotDescriptor: Descriptor(), FocusDescriptor {
  override fun cost() = Cost(0.0, COSTTYPE.ADDITIVE)

  override fun modifyFocus(targetable: Targetable): Targetable {
    if (targetable is InventoryTargetable) targetable.useFirstMatchingSlot(Predicate { obj: ItemStack -> obj.isEmpty })
    return targetable
  }
}
