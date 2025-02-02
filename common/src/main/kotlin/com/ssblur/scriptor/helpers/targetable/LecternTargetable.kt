package com.ssblur.scriptor.helpers.targetable

import com.ssblur.scriptor.blockentity.CastingLecternBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.Container
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f

@Suppress("unused")
class LecternTargetable: Targetable, InventoryTargetable {
  override var targetedSlot: Int

  constructor(level: Level, pos: Vec3): super(level, pos) {
    targetedSlot = 0
  }

  constructor(level: Level, pos: Vector3f): super(
    level,
    Vec3(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
  ) {
    targetedSlot = 0
  }

  constructor(level: Level, pos: BlockPos): super(level, pos) {
    targetedSlot = 0
  }

  override val container: Container? = null
    get() {
      if (level.getBlockEntity(targetBlockPos) is CastingLecternBlockEntity) {
        if (level.getBlockEntity(targetBlockPos.below()) is Container) return field
        else if (level.getBlockEntity(targetBlockPos.relative(this.facing.opposite)) is Container) return field
      }
      return null
    }

  override val origin: BlockPos
    get() {
      return targetBlockPos
    }
}
