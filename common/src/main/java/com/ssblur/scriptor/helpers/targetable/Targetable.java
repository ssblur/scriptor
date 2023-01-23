package com.ssblur.scriptor.helpers.targetable;

import com.mojang.math.Vector3f;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;

public class Targetable {
  Vec3 targetPos;

  /**
   * A class describing anything that can be targeted by a spell
   * Shall always describe at least a position
   * @param pos The position targeted by / closest to the target of this cast
   */
  public Targetable(Vec3 pos) {
    targetPos = pos;
  }

  public Targetable(Vector3f pos) {
    targetPos = new Vec3(pos.x(), pos.y(), pos.z());
  }

  public Vec3 getTargetPos() {
    return targetPos;
  }
}
