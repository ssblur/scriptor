package com.ssblur.scriptor.word.descriptor.focus.entity

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.focus.MultiTargetFocusDescriptor
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import net.minecraft.core.Direction
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

/**
 * Convert all currently targeted entities into block targets, using the entities positions
 */
class BlockOnlyDescriptor: Descriptor(), MultiTargetFocusDescriptor {
  override fun modifyTargetsFocus(originalTargetables: List<Targetable>, owner: Targetable): List<Targetable> {
    val targetables = mutableListOf<Targetable>()
    if (originalTargetables.isEmpty()) return targetables

    for (originalTargetable in originalTargetables) {
      if (originalTargetable is EntityTargetable) {
        val entityPos: Vec3 = originalTargetable.targetPos
//        Set facing direction to DOWN for consistency
        targetables.add(Targetable(originalTargetable.level, entityPos).setFacing(Direction.DOWN))
      } else {
        targetables.add(originalTargetable)
      }
    }
    return targetables
  }

  override fun replacesSubjectCost() = false
  override fun allowsDuplicates() = true
  override fun cost() = Cost(1.25, COSTTYPE.MULTIPLICATIVE)
}


