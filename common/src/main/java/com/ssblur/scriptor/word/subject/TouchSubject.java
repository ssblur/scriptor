package com.ssblur.scriptor.word.subject;

import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.messages.TraceNetwork;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class TouchSubject extends Subject{

  @Override
  public CompletableFuture<List<Targetable>> getTargets(Targetable caster, Spell spell) {
    var result = new CompletableFuture<List<Targetable>>();
    if(caster instanceof EntityTargetable entityTargetable && entityTargetable.getTargetEntity() instanceof Player player) {
      TraceNetwork.requestTraceData(player, target -> result.complete(List.of(target)));
    } else {
      result.complete(List.of());
    }
    return result;
  }

  @Override
  public Cost cost() { return new Cost(1, COSTTYPE.ADDITIVE); }
}
