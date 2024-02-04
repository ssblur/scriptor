package com.ssblur.scriptor.word.descriptor.target.inventory;

import com.ssblur.scriptor.helpers.targetable.InventoryTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.word.descriptor.target.TargetDescriptor;

import java.util.List;

public class FirstFilledSlotDescriptor extends Descriptor implements TargetDescriptor {
  @Override
  public Cost cost() {
    return new Cost(0, COSTTYPE.ADDITIVE);
  }

  @Override
  public List<Targetable> modifyTargets(List<Targetable> targetables) {
    targetables.forEach(targetable -> {
      if(targetable instanceof InventoryTargetable inventoryTargetable)
        inventoryTargetable.useFirstFilledSlot();
    });
    return targetables;
  }

  @Override
  public boolean replacesSubjectCost() {
    return false;
  }
}
