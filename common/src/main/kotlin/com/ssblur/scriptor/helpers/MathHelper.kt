package com.ssblur.scriptor.helpers

import com.ssblur.scriptor.helpers.targetable.Targetable
import net.minecraft.core.Direction
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3
import kotlin.math.PI
import kotlin.math.sin
import kotlin.math.cos
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
  /**
   * Calculate the number of blocks required to form the square's border
   * @param d: The diameter of the square
   * @return the integer number of blocks required to form the square's border
   */
  fun calculate_square_border_length(d: Int): Int {
    when(d) {
      0 -> return 0
      1 -> return 1
    }
    return (d*d) - ((d-2)*(d-2))
  }
  /**
   * Returns a set of 2d coordinates forming the perimiter of a square
   * @param diameter: The diameter of the square
   * @return A set of Vec2 coordinates forming the perimiter of a square
   */
  fun get_square_coords(diameter: Int): ArrayList<Vec2> {
//    Expect minimum 2x2 diameter square.
    assert(diameter > 1)
    var positions: ArrayList<Vec2> = arrayListOf<Vec2>()
    val borderLength = calculate_square_border_length(diameter)
    var pos: Pair<Float, Float>
    for (i in 1..borderLength) {
      when (i) {
        0 -> pos = Pair(1f, 0f)
        in 1..diameter -> pos = Pair(0f, (i-1).toFloat())
        in diameter+1..diameter*2 - 1 -> pos = Pair((i-diameter).toFloat(), (diameter-1).toFloat())
        in diameter*2..diameter*3 - 2 -> pos = Pair((diameter-1).toFloat(), (3*diameter-2-(i)).toFloat())
        else -> pos = Pair((4*diameter-(3+i)).toFloat(), 0f)
      }
      positions.add(Vec2(pos.first, pos.second))
    }

    return positions
  }
  /**
   * Rotates a point anticlockwise (in minecraft this seems to rotate clockwise)
   * @param point: The Vec2 point to rotate
   * @param degrees: The angle by which to rotate the point (degrees)
   * @return The Vec2 point rotated by degrees
   */
  fun rotate_point_anticlockwise(point: Vec2, degrees: Float): Vec2 {
    val radians = (degrees * (PI / 180f)).toFloat()
    return Vec2(
      point.x * cos(radians) - point.y * sin(radians),
      point.x * sin(radians) + point.y * cos(radians),
    )
  }
  /**
   * Transforms a point according to the player's horizontal view direction (North, East, South, West)
   * and the targeted face of the initial block target. This has the effect of affecting points relative
   * to the player's perspective, rather than the absolute coordinate system
   * @param initial_targetable: The first position targeted.
   * @param owner: The player casting the spell.
   * @param point: The point to transform according to player perspective
   * @return The transformed Vec3 point
   */
  fun player_view_transform_point(initial_targetable: Targetable, owner: Targetable, point: Vec2): Vec3 {

    val pos = initial_targetable.targetPos
    val axis = initial_targetable.facing.axis

    var newPos: Vec3
    if (axis === Direction.Axis.X) {
      var x_mult = if (owner.entityYRotation!!.toInt() in 180..360) {
        1
      } else {
        -1
      }
      newPos = Vec3(
        pos.x,
        pos.y + point.y,
        pos.z + point.x * x_mult
      )
    } else if (axis === Direction.Axis.Y) {
      var transformed_point = rotate_point_anticlockwise(point, owner.entityCoarseYRotation!! + 90)
      newPos = Vec3(
        pos.x + transformed_point.x,
        pos.y,
        pos.z + transformed_point.y
      )
    }
    else {
      var x_mult = if (owner.entityYRotation!!.toInt() in 90..270) {
        1
      } else {
        -1
      }
      newPos = Vec3(pos.x + point.x * x_mult, pos.y + point.y, pos.z)
    }
    return newPos
  }
}
