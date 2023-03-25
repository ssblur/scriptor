package com.ssblur.scriptor.word.descriptor;

import com.ssblur.scriptor.damage.SacrificeDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;


public class BloodCostDescriptor extends Descriptor implements CastDescriptor {
  @Override
  public Cost cost() { return new Cost(-2, COSTTYPE.ADDITIVE_POST); }

  @Override
  public boolean onCast(Entity caster) {
    if(caster instanceof LivingEntity living) {
      living.hurt(new SacrificeDamageSource(), 1.0f);
      living.invulnerableTime = 0;
      return living.isAlive();
    }
    return false;
  }

  @Override
  public boolean allowsDuplicates() {
    return true;
  }
}
