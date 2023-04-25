package com.ssblur.scriptor.helpers.targetable;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SpellbookTargetable extends ItemTargetable implements InventoryTargetable {
  int slot;
  public SpellbookTargetable(ItemStack itemStack, Player entity, int slot) {
    super(itemStack, entity);
    this.slot = slot;
  }

  @Override
  public @Nullable Container getContainer() {
    return ((Player) targetEntity).getInventory();
  }

  @Override
  public int getTargetedSlot() {
    return slot;
  }
}
