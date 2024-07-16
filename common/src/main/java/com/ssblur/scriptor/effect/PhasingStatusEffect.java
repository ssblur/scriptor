package com.ssblur.scriptor.effect;

import com.ssblur.scriptor.blockentity.PhasedBlockBlockEntity;
import net.minecraft.core.Vec3i;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class PhasingStatusEffect extends MobEffect {
  public PhasingStatusEffect() {
    super(MobEffectCategory.NEUTRAL, 0x2d0096);
  }

  public PhasingStatusEffect(MobEffectCategory mobEffectCategory, int i) {
    super(mobEffectCategory, i);
  }

  @Override
  public boolean applyEffectTick(LivingEntity entity, int amplifier) {
    if(entity instanceof Player player)
      phase(player, 0);
    return true;
  }

  public static void phase(Player entity, int bottom) {
    for (int x = 1; x >= -1; x--)
      for (int y = (int) Math.ceil(entity.getEyeHeight()); y >= bottom; y--)
        for (int z = 1; z >= -1; z--)
          PhasedBlockBlockEntity.phase(entity.level(), entity.blockPosition().offset(new Vec3i(x, y, z)));
  }

  @Override
  public boolean shouldApplyEffectTickThisTick(int i, int j) {
    return true;
  }
}
