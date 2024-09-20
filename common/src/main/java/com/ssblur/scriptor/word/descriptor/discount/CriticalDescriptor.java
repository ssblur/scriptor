package com.ssblur.scriptor.word.descriptor.discount;

import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.api.word.descriptor.CastDescriptor;
import net.minecraft.world.entity.LivingEntity;

public class CriticalDescriptor extends Descriptor implements CastDescriptor {
  @Override
  public Cost cost() {
    return Cost.multiply(0.3);
  }

  @Override
  public boolean cannotCast(Targetable caster) {
    return !(
      caster instanceof EntityTargetable entityTargetable
      && entityTargetable.getTargetEntity() instanceof LivingEntity entity
      && entity.getHealth() <= 2.0f
    );
  }
}
