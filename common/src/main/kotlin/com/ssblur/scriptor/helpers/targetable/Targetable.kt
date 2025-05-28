package com.ssblur.scriptor.helpers.targetable

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f
import kotlin.math.floor

@Suppress("unused")
open class Targetable {
  var targetPos: Vec3
  open val origin: BlockPos? = null
  var level: Level
  var direction: Direction? = null
  open var finalTargetable: Targetable? = this

  /**
   * A class describing anything that can be targeted by a spell
   * Shall always describe at least a position
   * @param pos The position targeted by / closest to the target of this cast
   */
  constructor(level: Level, pos: Vec3) {
    targetPos = pos
    this.level = level
  }

  constructor(level: Level, pos: Vector3f) {
    targetPos = Vec3(pos.x().toDouble(), pos.y().toDouble(), pos.z().toDouble())
    this.level = level
  }

  constructor(level: Level, pos: BlockPos) {
    targetPos = Vec3(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
    this.level = level
  }

  fun setTargetPos(targetPos: Vector3f) {
    this.targetPos = Vec3(targetPos.x().toDouble(), targetPos.y().toDouble(), targetPos.z().toDouble())
  }

  val targetBlockPos: BlockPos
    get() = BlockPos(floor(targetPos.x).toInt(), floor(targetPos.y).toInt(), floor(targetPos.z).toInt())

  val facing: Direction
    get() {
      if (direction != null) return direction!!
      return Direction.UP
    }

  open val entityYRotation: Float? = null

  open val entityCoarseYRotation: Float? = null

  fun setFacing(direction: Direction?): Targetable {
    this.direction = direction
    return this
  }

  val offsetBlockPos: BlockPos
    get() {
      if (direction != null) return targetBlockPos.relative(direction!!.opposite)
      return targetBlockPos
    }

  fun simpleCopy(): Targetable {
    val out = Targetable(level, targetPos)
    out.setFacing(facing)
    return out
  }
}
