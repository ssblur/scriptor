package com.ssblur.scriptor.word.descriptor.discount;

import com.ssblur.scriptor.damage.ScriptorDamage;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.CastDescriptor;
import com.ssblur.scriptor.api.word.Descriptor;
import net.minecraft.world.entity.LivingEntity;


public class BloodCostDescriptor extends Descriptor implements CastDescriptor {
  @Override
  public Cost cost() { return new Cost(-2, COSTTYPE.ADDITIVE_POST); }

  @Override
  public boolean cannotCast(Targetable caster) {
    if(caster instanceof EntityTargetable entityTargetable && entityTargetable.getTargetEntity() instanceof LivingEntity living) {
      living.hurt(ScriptorDamage.sacrifice(living), 1.0f);
      living.invulnerableTime = 0;
      return !living.isAlive();
    }
    return true;
  }

  @Override
  public boolean allowsDuplicates() {
    return true;
  }
}
