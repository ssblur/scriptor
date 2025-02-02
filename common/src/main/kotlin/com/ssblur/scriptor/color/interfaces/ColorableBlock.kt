package com.ssblur.scriptor.color.interfaces

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level

interface ColorableBlock: ColorableItem {
  fun setColor(color: Int, level: Level?, blockPos: BlockPos)
}
