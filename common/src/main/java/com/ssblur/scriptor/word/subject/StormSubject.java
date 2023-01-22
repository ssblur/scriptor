package com.ssblur.scriptor.word.subject;

import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Random;

public class StormSubject extends Subject{
  @Override
  public void cast(Entity caster, Spell spell) {
    Random random = new Random();
    int radius = 4;
    int limit = 12;
    BlockPos center = caster.blockPosition();
    BlockPos pos;

    for(int i = 0; i < limit; i++) {
      pos = center.offset(
        random.nextInt((radius * 2) - radius),
        3,
        random.nextInt((radius * 2) - radius)
      );
      for(int j = 0; j < 3; j++) {
        if(caster.level.getBlockState(pos.below()).getMaterial().isReplaceable())
          pos = pos.below();
        else
          break;
      }

      var entities = caster.level.getEntitiesOfClass(
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
          spell.action().apply(caster, new EntityTargetable(entity), spell.descriptors());
      else
        spell.action().apply(caster, new Targetable(pos), spell.descriptors());
    }
  }

  @Override
  public Cost cost() { return new Cost(8, COSTTYPE.MULTIPLICATIVE); }
}
