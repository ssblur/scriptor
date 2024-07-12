package com.ssblur.scriptor.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class WildPhasingStatusEffect extends MobEffect {
  public WildPhasingStatusEffect() {
    super(MobEffectCategory.HARMFUL, 0x50007f);
  }

  @Override
  public boolean applyEffectTick(LivingEntity entity, int amplifier) {
    if(!(entity instanceof Player)) {
      entity.setPos(entity.position().add(new Vec3(0, -0.05, 0)));
    }
    return true;
  }

  @Override
  public boolean shouldApplyEffectTickThisTick(int i, int j) {
    return true;
  }
}
