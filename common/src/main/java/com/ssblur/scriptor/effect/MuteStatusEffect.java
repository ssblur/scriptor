package com.ssblur.scriptor.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class MuteStatusEffect extends MobEffect {
  public MuteStatusEffect() {
    super(MobEffectCategory.HARMFUL, 0xcc00cc);
  }

  @Override
  public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {}
}
