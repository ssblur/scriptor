package com.ssblur.scriptor.neoforge.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;


public class BreakBlockExpectPlatformImpl {
  public static boolean canBreakBlock(LivingEntity entity, Level level, BlockPos pos) {
    if(entity instanceof Player player)
      return !NeoForge.EVENT_BUS.post(new BlockEvent.BreakEvent(level, pos, level.getBlockState(pos), player)).isCanceled();
    return false;
  }
}
