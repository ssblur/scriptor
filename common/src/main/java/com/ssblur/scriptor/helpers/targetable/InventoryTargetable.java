package com.ssblur.scriptor.helpers.targetable;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public interface InventoryTargetable {
  @Nullable
  Container getContainer();

  int getTargetedSlot();
  void setTargetedSlot(int slot);

  default void useFirstMatchingSlot(Predicate<ItemStack> predicate) {
    int slot = getFirstMatchingSlot(predicate);
    if(slot >= 0)
      setTargetedSlot(slot);
  }

  default int getFirstMatchingSlot(Predicate<ItemStack> predicate) {
    if(getContainer() != null)
      for(int i = 0; i < getContainer().getContainerSize(); i++)
        if(predicate.test(getContainer().getItem(i))) {
          return i;
        }
    return -1;
  }
  default void useFirstFilledSlot() {
    int slot = getFirstFilledSlot();
    if(slot >= 0)
      setTargetedSlot(slot);
  }

  default int getFirstFilledSlot() {
    if(getContainer() != null)
      for(int i = 0; i < getContainer().getContainerSize(); i++) {
        if (!getContainer().getItem(i).isEmpty()) {
          setTargetedSlot(i);
        }
      }
    return -1;
  }
}
