package com.ssblur.scriptor.helpers;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;

import java.util.logging.Level;

public class BreakBlockHelperExpectPlatform {
  @ExpectPlatform
  public static boolean canBreakBlock(LivingEntity entity, Level world, BlockPos pos) {
    throw new AssertionError();
  }
}
