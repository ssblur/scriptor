package com.ssblur.scriptor.word.descriptor.target.inventory;

import com.ssblur.scriptor.helpers.targetable.*;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.target.TargetDescriptor;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class FirstEmptySlotDescriptor extends Descriptor implements TargetDescriptor {
  @Override
  public Cost cost() {
    return new Cost(0, COSTTYPE.ADDITIVE);
  }

  @Override
  public List<Targetable> modifyTargets(List<Targetable> targetables) {
    targetables.forEach(targetable -> {
      if(targetable instanceof InventoryTargetable inventoryTargetable)
        inventoryTargetable.useFirstMatchingSlot(ItemStack::isEmpty);
    });
    return targetables;
  }

  @Override
  public boolean replacesSubjectCost() {
    return false;
  }
}
