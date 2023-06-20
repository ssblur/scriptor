package com.ssblur.scriptor.helpers;

import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.world.item.ItemStack;

public class SpellbookAccess extends BookViewScreen.WrittenBookAccess {
  public SpellbookAccess(ItemStack itemStack) {
    super(itemStack);
  }
}
