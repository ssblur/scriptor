package com.ssblur.scriptor.forge.helpers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;


public class BreakBlockExpectPlatformImpl {
  public static boolean canBreakBlock(LivingEntity entity, Level level, BlockPos pos) {
    if(entity instanceof Player player)
      return !MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(level, pos, level.getBlockState(pos), player));
    return false;
  }
}
