package com.ssblur.scriptor.color.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface ColorableBlock extends ColorableItem {
  void setColor(int color, Level level, BlockPos blockPos);
}
