package com.ssblur.scriptor.word.descriptor.target

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.MathHelper
import com.ssblur.scriptor.helpers.targetable.Targetable

class SquareDescriptor: Descriptor(), GeometricTargetDescriptor {
  override fun modifyTargets(originalTargetables: List<Targetable>, owner: Targetable, descriptors: Array<Descriptor>): List<Targetable> {
    val targetables = originalTargetables.toMutableList()
    val uses = descriptors.map{ it is SquareDescriptor }.count{ it }
    if (targetables.isEmpty()) return targetables

    val square_points = MathHelper.get_square_coords(uses+1)
    val targetable = targetables[0]

    for (point in square_points) {
      var transformed_point = MathHelper.player_view_transform_point(targetable, owner, point)
      targetables.add(Targetable(targetable.level, transformed_point).setFacing(targetable.facing))
    }

    return targetables

  }

  override fun replacesSubjectCost() = false
  override fun allowsDuplicates() = true
  override fun cost() = Cost(1.25, COSTTYPE.MULTIPLICATIVE)

}
