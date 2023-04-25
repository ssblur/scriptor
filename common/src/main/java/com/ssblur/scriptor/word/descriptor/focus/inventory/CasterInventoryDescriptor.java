package com.ssblur.scriptor.word.descriptor.focus.inventory;

import com.ssblur.scriptor.helpers.targetable.*;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.focus.FocusDescriptor;
import com.ssblur.scriptor.word.descriptor.target.TargetDescriptor;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CasterInventoryDescriptor  extends Descriptor implements FocusDescriptor {
  @Override
  public Cost cost() {
    return new Cost(0, COSTTYPE.ADDITIVE);
  }

  @Override
  public Targetable modifyFocus(Targetable targetable) {
    if(targetable instanceof ContainerTargetable)
      return targetable;
    if(targetable instanceof EntityTargetable entityTargetable)
      return new InventoryEntityTargetable(entityTargetable.getTargetEntity(), 0);
    if(targetable.getLevel().getBlockEntity(targetable.getTargetBlockPos()) instanceof Container)
      return new ContainerTargetable(targetable.getLevel(), targetable.getTargetBlockPos(), 0);
    return targetable;
  }
}
