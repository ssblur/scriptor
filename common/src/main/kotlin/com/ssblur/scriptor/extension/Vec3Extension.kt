package com.ssblur.scriptor.extension

import net.minecraft.core.BlockPos
import net.minecraft.world.phys.Vec3
import kotlin.math.roundToInt

object Vec3Extension {
  val Vec3.blockPos
    get() = BlockPos(this.x.roundToInt(), this.y.roundToInt(), this.z.roundToInt())
}