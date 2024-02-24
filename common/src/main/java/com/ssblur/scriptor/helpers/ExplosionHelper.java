package com.ssblur.scriptor.helpers;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ExplosionHelper {
  public static void explode(Level level, Vec3 pos, float strength) {
    if(strength < 10)
      level.explode(null, pos.x, pos.y, pos.z, strength, Level.ExplosionInteraction.MOB);
    else {
      level.explode(null, pos.x, pos.y, pos.z, 10, Level.ExplosionInteraction.MOB);
      // schedule block removal
    }
  }
}
