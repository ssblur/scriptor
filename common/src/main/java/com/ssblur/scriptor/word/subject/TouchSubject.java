package com.ssblur.scriptor.word.subject;

import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.LecternTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.events.network.TraceNetwork;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TouchSubject extends Subject{

  @Override
  public CompletableFuture<List<Targetable>> getTargets(Targetable caster, Spell spell) {
    var result = new CompletableFuture<List<Targetable>>();
    if(caster instanceof EntityTargetable entityTargetable && entityTargetable.getTargetEntity() instanceof Player player) {
      TraceNetwork.requestTraceData(player, target -> result.complete(List.of(target)));
    } else if(caster instanceof LecternTargetable) {
      var pos = caster.getTargetBlockPos().relative(caster.getFacing());
      var entities = caster.getLevel().getEntitiesOfClass(
        LivingEntity.class,
        AABB.ofSize(
          new Vec3(
            pos.getX(),
            pos.getY(),
            pos.getZ()
          ),
          1,
          1,
          1
        )
      );

      if(entities.isEmpty())
        result.complete(List.of(new Targetable(caster.getLevel(), pos)));
      else
        result.complete(entities.stream().map(living -> (Targetable) new EntityTargetable(living)).toList());
    } else {
      result.complete(List.of(caster.simpleCopy()));
    }
    return result;
  }

  @Override
  public Cost cost() { return new Cost(1, COSTTYPE.ADDITIVE); }
}
