package com.ssblur.scriptor.helpers.targetable;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface InventoryTargetable {
  @Nullable
  Container getContainer();

  int getTargetedSlot();
  void setTargetedSlot(int slot);

  default void useFirstMatchingSlot(ItemStack itemStack) {
    if(getContainer() != null)
      for(int i = 0; i < getContainer().getContainerSize(); i++)
        if(itemStack == ItemStack.EMPTY && getContainer().getItem(i).isEmpty()) {
          setTargetedSlot(i);
          return;
        } else if(getContainer().getItem(i).sameItemStackIgnoreDurability(itemStack)) {
          setTargetedSlot(i);
          return;
        }
  }
  default void useFirstFilledSlot() {
    if(getContainer() != null)
      for(int i = 0; i < getContainer().getContainerSize(); i++) {
        if (!getContainer().getItem(i).isEmpty()) {
          setTargetedSlot(i);
        }
      }
  }
}
