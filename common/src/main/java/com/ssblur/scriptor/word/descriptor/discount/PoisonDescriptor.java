package com.ssblur.scriptor.word.descriptor.discount;

import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.api.word.descriptor.CastDescriptor;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class PoisonDescriptor extends Descriptor implements CastDescriptor {
  @Override
  public Cost cost() {
    return new Cost(0.7d, COSTTYPE.MULTIPLICATIVE);
  }

  @Override
  public boolean cannotCast(Targetable caster) {
    if(caster instanceof EntityTargetable entityTargetable && entityTargetable.getTargetEntity() instanceof LivingEntity living)
      return !living.hasEffect(MobEffects.POISON);
    return true;
  }
}
