package com.ssblur.scriptor.word.subject;

import com.ssblur.scriptor.api.word.Subject;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class StormSubject extends Subject {

  @Override
  public Cost cost() { return new Cost(8, COSTTYPE.MULTIPLICATIVE); }

  @Override
  public CompletableFuture<List<Targetable>> getTargets(Targetable caster, Spell spell) {
    ArrayList<Targetable> targets = new ArrayList<>();
    Random random = new Random();
    int radius = 4;
    int limit = 12;
    BlockPos center = caster.getTargetBlockPos();
    BlockPos pos;

    for(int i = 0; i < limit; i++) {
      pos = center.offset(
        random.nextInt((radius * 2) - radius),
        3,
        random.nextInt((radius * 2) - radius)
      );
      for(int j = 0; j < 3; j++) {
        if(caster.getLevel().getBlockState(pos.below()).canBeReplaced())
          pos = pos.below();
        else
          break;
      }

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

      if(entities.size() > 0)
        for(var entity: entities)
          targets.add(new EntityTargetable(entity));
      else
        targets.add(new Targetable(caster.getLevel(), pos));
    }

    var result = new CompletableFuture<List<Targetable>>();
    result.complete(targets);
    return result;
  }
}
