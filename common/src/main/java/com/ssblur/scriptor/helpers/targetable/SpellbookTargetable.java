package com.ssblur.scriptor.helpers.targetable;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SpellbookTargetable extends ItemTargetable{
  int slot;
  public SpellbookTargetable(ItemStack itemStack, Player entity, int slot) {
    super(itemStack, entity);
    this.slot = slot;
  }

  public Player getPlayer() {
    if(targetEntity instanceof Player player)
      return player;
    return null;
  }
}
