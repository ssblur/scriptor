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

  default boolean shouldIgnoreTargetedSlot() {
    return getTargetedSlot() == -1 && getContainer() != null;
  }

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

  default int getFirstFilledSlot() {
    return getFirstMatchingSlot(item -> !item.isEmpty());
  }

  default int getFirstMatchingSlot(ItemStack itemStack) {
    return getFirstMatchingSlot(
      item -> item.isEmpty()
        || (
          ItemStack.isSameItemSameTags(item, itemStack)
            && (item.getCount() + itemStack.getCount()) <= item.getMaxStackSize()
      )
    );
  }

  default void useFirstFilledSlot() {
    int slot = getFirstFilledSlot();
    if(slot >= 0)
      setTargetedSlot(slot);
  }
}
