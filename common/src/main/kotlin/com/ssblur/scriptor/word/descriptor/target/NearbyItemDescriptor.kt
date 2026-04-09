package com.ssblur.scriptor.word.descriptor.target

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.ItemEntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.phys.AABB

object NearbyItemDescriptor: Descriptor(), TargetDescriptor {
  override fun modifyTargets(originalTargetables: List<Targetable>, owner: Targetable): List<Targetable> {
    return originalTargetables.flatMap {
      it.level.getEntitiesOfClass(
        ItemEntity::class.java,
        AABB.ofSize(it.targetPos, 1.5, 2.0, 1.5),
      ) ?: listOf()
    }.filterNotNull().map {
      ItemEntityTargetable(it)
    }
  }

  override fun cost() = Cost(0.05, COSTTYPE.ADDITIVE)
  override fun replacesSubjectCost() = false
  override fun allowsDuplicates() = true
}
