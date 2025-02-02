package com.ssblur.scriptor.helpers

import net.minecraft.world.phys.Vec2
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

object MathHelper {
  /**
   * Returns relative coordinates from center to reach a specified position in a square spiral from origin.
   * @param position The step in the spiral
   * @return A pair of integers which represent the offset from origin of this step in the spiral.
   */
  fun spiral(position: Int): Vec2 {
    assert(position > 0)
    val sqrt = ceil(sqrt(position.toDouble())).toInt()
    val ring = floor((sqrt.toDouble()) / 2.0).toInt()
    var x = 0
    var y = ring

    var stage = 0
    for (p in ((ring - 1) * 2 + 1).toDouble().pow(2.0).toInt() + 1 until position) {
      if (stage == 0) {
        x++
        if (x == ring) stage++
      } else if (stage == 1) {
        y--
        if (y == -ring) stage++
      } else if (stage == 2) {
        x--
        if (x == -ring) stage++
      } else if (stage == 3) {
        y++
        if (y == ring) stage++
      } else {
        x++
      }
    }
    return Vec2(x.toFloat(), y.toFloat())
  }
}
