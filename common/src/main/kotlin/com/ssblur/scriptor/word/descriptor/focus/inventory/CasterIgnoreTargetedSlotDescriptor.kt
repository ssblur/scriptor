package com.ssblur.scriptor.word.descriptor.focus.inventory

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.InventoryTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.focus.FocusDescriptor

class CasterIgnoreTargetedSlotDescriptor: Descriptor(), FocusDescriptor {
  override fun cost() = Cost(0.0, COSTTYPE.ADDITIVE)

  override fun modifyFocus(targetable: Targetable): Targetable {
    if (targetable is InventoryTargetable) targetable.targetedSlot = -1
    return targetable
  }
}
