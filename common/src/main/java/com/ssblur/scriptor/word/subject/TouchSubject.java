package com.ssblur.scriptor.word.subject;

import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.messages.TouchNetwork;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class TouchSubject extends Subject{
  record TouchQueue(Entity caster, CompletableFuture<List<Targetable>> future){}
  static HashMap<UUID, TouchQueue> touchQueue = new HashMap<>();

  @Override
  public CompletableFuture<List<Targetable>> getTargets(Entity caster, Spell spell) {
    var result = new CompletableFuture<List<Targetable>>();
    if(caster instanceof Player player) {
      UUID uuid = UUID.randomUUID();
      touchQueue.put(uuid, new TouchQueue(player, result));
      TouchNetwork.requestTouchData(player, uuid);
    } else {
      result.complete(List.of());
    }
    return result;
  }

  public void cast(Entity caster, Spell spell) {
  }

  public static void castFromQueue(UUID uuid, Targetable targetable, Player player) {
    if(isPlayerInvalid(uuid, player)) return;
    var spell = touchQueue.get(uuid);
    touchQueue.remove(uuid);
    spell.future().complete(List.of(targetable));
  }

  public static void dropFromQueue(UUID uuid, Player player) {
    if(isPlayerInvalid(uuid, player)) return;

    touchQueue.remove(uuid);
  }

  public static boolean isPlayerInvalid(UUID uuid, Player player) {
    return !touchQueue.get(uuid).caster.getUUID().equals(player.getUUID());
  }

  @Override
  public Cost cost() { return new Cost(1, COSTTYPE.ADDITIVE); }
}
