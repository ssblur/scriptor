package com.ssblur.scriptor.word.descriptor.power;

import com.ssblur.scriptor.effect.ScriptorEffects;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.CastDescriptor;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class OverwhelmingStrengthDescriptor extends Descriptor implements CastDescriptor, StrengthDescriptor {
  static Cost COST = new Cost(0, COSTTYPE.ADDITIVE);

  public OverwhelmingStrengthDescriptor() {}

  @Override
  public Cost cost() {
    return COST;
  }

  @Override
  public double strengthModifier() {
    return 20;
  }

  @Override
  public boolean allowsDuplicates() {
    return false;
  }

  @Override
  public boolean cannotCast(Targetable caster) {
    if(caster instanceof EntityTargetable entityTargetable && entityTargetable.getTargetEntity() instanceof LivingEntity living) {
      living.addEffect(new MobEffectInstance(ScriptorEffects.MUTE.get(), 20 * 60));
      return false;
    }
    return true;
  }
}
