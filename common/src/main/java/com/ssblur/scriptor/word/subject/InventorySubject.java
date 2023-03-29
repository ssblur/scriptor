package com.ssblur.scriptor.word.subject;

import com.ssblur.scriptor.word.Spell;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public interface InventorySubject {
  void castOnItem(Spell spell, Player player, ItemStack slot);
}
