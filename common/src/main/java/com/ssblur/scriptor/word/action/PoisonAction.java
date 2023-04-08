package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.duration.DurationDescriptor;
import com.ssblur.scriptor.word.descriptor.power.StrengthDescriptor;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class PoisonAction extends Action {
  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    double strength = 4;
    double duration = 2;
    for(var d: descriptors) {
      if(d instanceof StrengthDescriptor strengthDescriptor)
        strength += strengthDescriptor.strengthModifier();
      if(d instanceof DurationDescriptor durationDescriptor)
        duration += durationDescriptor.durationModifier();
    }

    strength = Math.max(strength, 0);
    strength /= 3;
    duration *= 60;

    // Maybe add poison-tipped enchant?

    if(targetable instanceof EntityTargetable entityTargetable && entityTargetable.getTargetEntity() instanceof LivingEntity living)
      living.addEffect(new MobEffectInstance(MobEffects.POISON, (int) duration, (int) strength));
  }
  @Override
  public Cost cost() { return new Cost(4, COSTTYPE.ADDITIVE); }
}
