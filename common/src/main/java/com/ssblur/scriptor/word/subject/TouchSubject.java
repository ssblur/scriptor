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
import java.util.UUID;

public class TouchSubject extends Subject{
  record TouchQueue(Entity caster, Spell spell){}
  static HashMap<UUID, TouchQueue> touchQueue = new HashMap<>();

  @Override
  public void cast(Entity caster, Spell spell) {
    if(caster instanceof Player player) {
      UUID uuid = UUID.randomUUID();
      touchQueue.put(uuid, new TouchQueue(player, spell));
      TouchNetwork.requestTouchData(player, uuid);
    }
  }

  public static void castFromQueue(UUID uuid, Targetable targetable, Player player) {
    if(isPlayerInvalid(uuid, player)) return;
    var spell = touchQueue.get(uuid);
    touchQueue.remove(uuid);
    spell.spell.action().apply(spell.caster(), targetable, spell.spell().deduplicatedDescriptors());
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
