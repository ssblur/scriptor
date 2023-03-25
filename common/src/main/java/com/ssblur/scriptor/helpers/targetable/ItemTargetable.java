package com.ssblur.scriptor.helpers.targetable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ItemTargetable extends EntityTargetable {
  ItemStack itemStack;

  public ItemTargetable(ItemStack itemStack, Player entity) {
    super(entity);
    this.itemStack = itemStack;
  }

  public ItemStack getTargetItem() {
    return itemStack;
  }
}
