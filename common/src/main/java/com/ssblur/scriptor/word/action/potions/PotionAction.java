package com.ssblur.scriptor.word.action.potions;

import com.ssblur.scriptor.api.word.Action;
import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.duration.DurationDescriptor;
import com.ssblur.scriptor.word.descriptor.power.StrengthDescriptor;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class PotionAction extends Action {
  MobEffect mobEffect;
  double durationScale;
  double strengthScale;
  Cost cost;
  public PotionAction(MobEffect mobEffect, double durationScale, double strengthScale, Cost cost) {
    this.mobEffect = mobEffect;
    this.durationScale = durationScale;
    this.strengthScale = strengthScale;
    this.cost = cost;
  }

  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    double strength = 0;
    double duration = 2;
    for(var d: descriptors) {
      if(d instanceof StrengthDescriptor strengthDescriptor)
        strength += strengthDescriptor.strengthModifier();
      if(d instanceof DurationDescriptor durationDescriptor)
        duration += durationDescriptor.durationModifier();
    }

    strength = Math.max(strength, 0);
    strength *= strengthScale;
    duration *= durationScale;

    // Maybe add poison-tipped enchant?

    if(targetable instanceof EntityTargetable entityTargetable && entityTargetable.getTargetEntity() instanceof LivingEntity living)
      living.addEffect(new MobEffectInstance(mobEffect, (int) Math.round(duration), (int) Math.floor(strength)));
    else
      applyToPosition(caster, targetable, descriptors, strength, duration);
  }

  public void applyToPosition(Targetable caster, Targetable targetable, Descriptor[] descriptors, double strength, double duration) {}

  @Override
  public Cost cost() {
    return this.cost;
  }
}
