package com.ssblur.scriptor.effect;

import com.ssblur.scriptor.blockentity.PhasedBlockBlockEntity;
import net.minecraft.core.Vec3i;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class PhasingStatusEffect extends MobEffect {
  public PhasingStatusEffect() {
    super(MobEffectCategory.NEUTRAL, 0x2d0096);
  }

  @Override
  public void applyEffectTick(LivingEntity entity, int amplifier) {}
}
