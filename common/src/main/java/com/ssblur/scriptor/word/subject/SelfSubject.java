package com.ssblur.scriptor.word.subject;

import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SelfSubject extends Subject{
  @Override
  public Cost cost() { return new Cost(1, COSTTYPE.ADDITIVE); }

  @Override
  public CompletableFuture<List<Targetable>> getTargets(Entity caster, Spell spell) {
    var result = new CompletableFuture<List<Targetable>>();
    result.complete(List.of(new EntityTargetable(caster).setFacing(Direction.DOWN)));
    return result;
  }
}
