package com.ssblur.scriptor.word.descriptor.target

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.MathHelper
import com.ssblur.scriptor.helpers.targetable.Targetable
import net.minecraft.core.Direction
import net.minecraft.world.phys.Vec2

class CircleDescriptor: Descriptor(), GeometricTargetDescriptor {
  override fun modifyTargets(originalTargetables: List<Targetable>, owner: Targetable, descriptors: Array<Descriptor>): List<Targetable> {
    val targetables = originalTargetables.toMutableList()
    val uses = descriptors.map{ it is CircleDescriptor }.count{ it }
    if (targetables.isEmpty()) return targetables
    val radius = uses
    val circle_points = MathHelper.get_circle_coords(radius)
    val targetable = targetables[0]
    val axis = targetable.facing.axis

    for (point in circle_points) {
      //    Translate positions so initial coordinate is in top center. This allows placing empty circles without the center
      //    point being filled in
      var translated_point = if (axis === Direction.Axis.Y) {
        Vec2(point.x + radius, point.y)
      } else {
        Vec2(point.x, point.y + radius)
      }
      var transformed_point = MathHelper.player_view_transform_point(targetable, owner, translated_point)
      targetables.add(Targetable(targetable.level, transformed_point).setFacing(targetable.facing))
    }

    return targetables

  }

  override fun replacesSubjectCost() = false
  override fun allowsDuplicates() = true
  override fun cost() = Cost(1.25, COSTTYPE.MULTIPLICATIVE)

}
