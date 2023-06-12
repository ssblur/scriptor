package com.ssblur.scriptor.helpers.targetable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class Targetable {
  Vec3 targetPos;
  BlockPos origin;
  Level level;
  Direction direction = null;
  Targetable finalTargetable;

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
  public void setTargetPos(Vec3 targetPos) {
    this.targetPos = targetPos;
  }

  public void setTargetPos(Vector3f targetPos) {
    this.targetPos = new Vec3(targetPos.x(), targetPos.y(), targetPos.z());
  }

  public BlockPos getTargetBlockPos() {
    return new BlockPos((int) targetPos.x, (int) targetPos.y, (int) targetPos.z);
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

  public BlockPos getOrigin() {
    return origin;
  }

  public void setOrigin(BlockPos origin) {
    this.origin = origin;
  }

  public void setFinalTargetable(Targetable targetable) {
    this.finalTargetable = targetable;
  }

  public Targetable getFinalTargetable() {
    if(finalTargetable != null)
      return finalTargetable;
    return this;
  }

  public Targetable simpleCopy() {
    return new Targetable(level, targetPos);
  }
}
