package com.ssblur.scriptor.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EmpoweredStatusEffect extends MobEffect {
  float scale;
  public EmpoweredStatusEffect(float scale) {
    super(MobEffectCategory.BENEFICIAL, 0x565656);
    this.scale = scale;
  }

  @Override
  public boolean applyEffectTick(LivingEntity entity, int amplifier) {
    return true;
  }

  public float getScale() {
    return scale;
  }
}
