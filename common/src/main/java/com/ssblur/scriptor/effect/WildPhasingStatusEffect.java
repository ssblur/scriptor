package com.ssblur.scriptor.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class WildPhasingStatusEffect extends MobEffect {
  public WildPhasingStatusEffect() {
    super(MobEffectCategory.HARMFUL, 0x50007f);
  }

  @Override
  public void applyEffectTick(LivingEntity entity, int amplifier) {}
}
