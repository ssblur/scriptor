package com.ssblur.scriptor.word.descriptor.focus.inventory;

import com.ssblur.scriptor.helpers.targetable.InventoryTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.focus.FocusDescriptor;
import net.minecraft.world.item.ItemStack;

public class CasterIgnoreTargetedSlotDescriptor extends Descriptor implements FocusDescriptor {
  @Override
  public Cost cost() {
    return new Cost(0, COSTTYPE.ADDITIVE);
  }

  @Override
  public Targetable modifyFocus(Targetable targetable) {
    if(targetable instanceof InventoryTargetable inventoryTargetable)
      inventoryTargetable.setTargetedSlot(-1);
    return targetable;
  }
}
