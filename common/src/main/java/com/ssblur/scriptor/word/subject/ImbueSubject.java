package com.ssblur.scriptor.word.subject;

import com.ssblur.scriptor.helpers.targetable.ItemTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ImbueSubject extends Subject implements InventorySubject {
  @Override
  public Cost cost() {
    return new Cost(10, COSTTYPE.MULTIPLICATIVE);
  }

  @Override
  public CompletableFuture<List<Targetable>> getTargets(Entity caster, Spell spell) {
    if(caster instanceof Player player) {
      player.sendSystemMessage(Component.translatable("extra.scriptor.enchant_wrong"));
    }
    CompletableFuture<List<Targetable>> future = new CompletableFuture<>();
    future.complete(List.of());
    return future;
  }

  @Override
  public void castOnItem(Spell spell, Player player, ItemStack slot) {
    var target = new ItemTargetable(slot, player);
    spell.cast(player, target);
  }
}
