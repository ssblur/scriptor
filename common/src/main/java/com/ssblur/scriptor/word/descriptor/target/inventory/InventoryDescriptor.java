package com.ssblur.scriptor.word.descriptor.target.inventory;

import com.ssblur.scriptor.helpers.MathHelper;
import com.ssblur.scriptor.helpers.targetable.ContainerTargetable;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.InventoryEntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.target.TargetDescriptor;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InventoryDescriptor extends Descriptor implements TargetDescriptor {
  @Override
  public Cost cost() {
    return new Cost(0, COSTTYPE.ADDITIVE);
  }

  @Override
  public List<Targetable> modifyTargets(List<Targetable> targetables) {
    return targetables.stream().map(targetable -> {
      if(targetable instanceof EntityTargetable entityTargetable)
        return new InventoryEntityTargetable(entityTargetable.getTargetEntity(), 0);
      if(targetable.getLevel().getBlockEntity(targetable.getOffsetBlockPos()) instanceof Container)
        return new ContainerTargetable(targetable.getLevel(), targetable.getOffsetBlockPos(), 0);
      return targetable;
    }).toList();
  }

  @Override
  public boolean replacesSubjectCost() {
    return false;
  }
}
