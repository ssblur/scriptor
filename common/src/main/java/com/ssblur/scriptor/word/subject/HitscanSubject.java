package com.ssblur.scriptor.word.subject;

import com.ssblur.scriptor.api.word.Subject;
import com.ssblur.scriptor.events.network.TraceNetwork;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.LecternTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HitscanSubject extends Subject {

  @Override
  public CompletableFuture<List<Targetable>> getTargets(Targetable caster, Spell spell) {
    var result = new CompletableFuture<List<Targetable>>();
    if(caster instanceof EntityTargetable entityTargetable && entityTargetable.getTargetEntity() instanceof Player player) {
      TraceNetwork.requestExtendedTraceData(player, target -> result.complete(List.of(target)));
    } else if(caster instanceof LecternTargetable) {
      var pos = caster.getTargetBlockPos().relative(caster.getFacing());
      var normal = caster.getFacing().getNormal();
      int x = normal.getX() * 19;
      int z = normal.getZ() * 19;
      var entities = caster.getLevel().getEntitiesOfClass(
        LivingEntity.class,
        AABB.ofSize(
          new Vec3(
            pos.getX(),
            pos.getY(),
            pos.getZ()
          ),
          1 + x,
          1,
          1 + z
        )
      );

      if(entities.isEmpty()) {
        int i = 0;
        x = caster.getFacing().getStepX();
        z = caster.getFacing().getStepZ();
        while(i < 19) {
          if(
            !caster.getLevel().getBlockState(
              pos.offset(x * i, 0, z * i)
            ).isAir()
          ) {
            result.complete(List.of(new Targetable(caster.getLevel(), pos.offset(x * i, 0, z * i))));
            return result;
          }
          i++;
        }

      } else
        result.complete(entities.stream().map(living -> (Targetable) new EntityTargetable(living)).toList());
    } else {
      result.complete(List.of(caster.simpleCopy()));
    }
    return result;
  }

  @Override
  public Cost cost() { return new Cost(8, COSTTYPE.ADDITIVE); }
}
