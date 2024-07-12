package com.ssblur.scriptor.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class PhasingStatusEffect extends MobEffect {
  public PhasingStatusEffect() {
    super(MobEffectCategory.NEUTRAL, 0x2d0096);
  }

  @Override
  public boolean applyEffectTick(LivingEntity entity, int amplifier) {
    return true;
  }
}
