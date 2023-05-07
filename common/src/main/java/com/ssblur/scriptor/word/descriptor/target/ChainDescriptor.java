package com.ssblur.scriptor.word.descriptor.target;

import com.ssblur.scriptor.helpers.MathHelper;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChainDescriptor extends Descriptor implements TargetDescriptor {
  @Override
  public Cost cost() {
    return new Cost(1.25d, COSTTYPE.MULTIPLICATIVE);
  }

  @Override
  public List<Targetable> modifyTargets(List<Targetable> targetables) {
    if(targetables.size() == 0) return targetables;
    targetables = new ArrayList<>(targetables);

    Random random = new Random();
    if(targetables.get(random.nextInt(targetables.size())) instanceof EntityTargetable entityTargetable) {
      var pos = entityTargetable.getTargetPos();
      var entities = entityTargetable.getLevel().getEntitiesOfClass(
        LivingEntity.class,
        AABB.ofSize(
          new Vec3(
            pos.x() - 1,
            pos.y() - 1,
            pos.z() - 1
          ),
          3,
          3,
          3
        )
      );
      if(entities.size() > 1) {
        LivingEntity newTarget;
        do
          newTarget = entities.get(random.nextInt(entities.size()));
        while(newTarget != entityTargetable.getTargetEntity());
        targetables.add(new EntityTargetable(newTarget));
      }
      return targetables;
    }

    Vec2 offset = MathHelper.spiral(targetables.size() + 1);
    var axis = targetables.get(0).getFacing().getAxis();
    var pos = targetables.get(0).getTargetPos();
    Vec3 newPos;
    if (axis == Direction.Axis.X)
      newPos = new Vec3(pos.x, pos.y + offset.y, pos.z + offset.x);
    else if (axis == Direction.Axis.Y)
      newPos = new Vec3(pos.x + offset.x, pos.y, pos.z + offset.y);
    else
      newPos = new Vec3(pos.x + offset.x, pos.y + offset.y, pos.z);
    var targetable = targetables.get(0);
    targetables.add(new Targetable(targetable.getLevel(), newPos).setFacing(targetable.getFacing()));

    return targetables;
  }

  @Override
  public boolean replacesSubjectCost() {
    return false;
  }

  @Override
  public boolean allowsDuplicates() { return true; }
}
