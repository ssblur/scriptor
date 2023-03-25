package com.ssblur.scriptor.word.subject;

import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EnchantSubject extends Subject {
  @Override
  public Cost cost() {
    return new Cost(40, COSTTYPE.MULTIPLICATIVE);
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
}
