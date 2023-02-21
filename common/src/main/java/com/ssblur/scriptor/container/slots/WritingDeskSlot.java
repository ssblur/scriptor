package com.ssblur.scriptor.container.slots;

import com.ssblur.scriptor.container.WritingDeskContainer;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class WritingDeskSlot extends Slot {
  WritingDeskContainer deskContainer;
  public WritingDeskSlot(WritingDeskContainer deskContainer, Container container, int i, int j, int k) {
    super(container, i, j, k);
    this.deskContainer = deskContainer;
  }

  public boolean mayPlace(ItemStack itemStack) {
    return true;
  }

  public int getMaxStackSize(ItemStack itemStack) {
    return super.getMaxStackSize(itemStack);
  }

}
