package com.ssblur.scriptor.fabric.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;

import java.util.logging.Level;

public class BreakBlockExpectPlatformImpl {
  public static boolean canBreakBlock(LivingEntity entity, Level world, BlockPos pos) {
    return true;
  }
}
