package com.ssblur.scriptor.helpers.targetable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ItemTargetable extends EntityTargetable {
  ItemStack itemStack;
  boolean targetItemByDefault;

  public ItemTargetable(ItemStack itemStack, Player entity) {
    super(entity);
    this.itemStack = itemStack;
    this.targetItemByDefault = true;
  }

  public ItemStack getTargetItem() {
    return itemStack;
  }

  public boolean shouldTargetItemByDefault() { return targetItemByDefault; }

  public ItemTargetable withTargetItemByDefault(boolean targetItemByDefault) {
    this.targetItemByDefault = targetItemByDefault;
    return this;
  }
}
