package com.ssblur.scriptor.helpers.targetable;

import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class Targetable {
  Vec3 targetPos;
  Level level;
  Direction direction = null;

  /**
   * A class describing anything that can be targeted by a spell
   * Shall always describe at least a position
   * @param pos The position targeted by / closest to the target of this cast
   */
  public Targetable(Level level, Vec3 pos) {
    targetPos = pos;
    this.level = level;
  }

  public Targetable(Level level, Vector3f pos) {
    targetPos = new Vec3(pos.x(), pos.y(), pos.z());
    this.level = level;
  }

  public Targetable(Level level, BlockPos pos) {
    targetPos = new Vec3(pos.getX(), pos.getY(), pos.getZ());
    this.level = level;
  }

  public Vec3 getTargetPos() {
    return targetPos;
  }

  public BlockPos getTargetBlockPos() {
    return new BlockPos(targetPos.x, targetPos.y, targetPos.z);
  }

  public Level getLevel() { return level; }

  public Direction getFacing() {
    if(direction != null)
      return direction;
    return Direction.UP;
  }

  public Targetable setFacing(@Nullable Direction direction) {
    this.direction = direction;
    return this;
  }

  public BlockPos getOffsetBlockPos() {
    if(direction != null)
      return getTargetBlockPos().relative(direction.getOpposite());
    return getTargetBlockPos();
  }
}
