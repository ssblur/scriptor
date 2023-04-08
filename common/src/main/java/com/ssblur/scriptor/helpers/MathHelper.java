package com.ssblur.scriptor.helpers;

import net.minecraft.world.phys.Vec2;
import org.checkerframework.checker.nullness.qual.NonNull;
import oshi.util.tuples.Pair;

public class MathHelper {
  /**
   * Returns relative coordinates from center to reach a specified position in a square spiral from origin.
   * @param position The step in the spiral
   * @return A pair of integers which represent the offset from origin of this step in the spiral.
   */
  @NonNull
  public static Vec2 spiral(int position) {
    assert position > 0;
    int sqrt = (int) Math.ceil(Math.sqrt(position));
    int ring = (int) Math.floor(((double) sqrt) / (double) 2);
    int x = 0;
    int y = ring;

    int stage = 0;
    for(int p = (int) Math.pow((ring - 1) * 2 + 1, 2) + 1; p < position; p++) {
      if(stage == 0) {
        x++;
        if(x == ring) stage++;
      } else if(stage == 1) {
        y--;
        if(y == -ring) stage++;
      } else if(stage == 2) {
        x--;
        if(x == -ring) stage++;
      } else if(stage == 3) {
        y++;
        if(y == ring) stage++;
      } else {
        x++;
      }
    }
    return new Vec2(x, y);
  }
}
