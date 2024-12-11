package com.ssblur.scriptor.word.descriptor.target.inventory

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.InventoryTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.target.TargetDescriptor
import net.minecraft.world.item.ItemStack

class FirstEmptySlotDescriptor: Descriptor(), TargetDescriptor {
  @Override
  override fun cost(): Cost {
    return Cost(0.0, COSTTYPE.ADDITIVE)
  }

  @Override
  override fun modifyTargets(originalTargetables: List<Targetable>, owner: Targetable): List<Targetable> {
    originalTargetables.forEach{
      if(it is InventoryTargetable)
          (it as InventoryTargetable).useFirstMatchingSlot(ItemStack::isEmpty)
    }
    return originalTargetables
  }

  @Override
  override fun replacesSubjectCost() = false
}
