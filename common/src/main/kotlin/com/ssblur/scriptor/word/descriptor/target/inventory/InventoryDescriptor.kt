package com.ssblur.scriptor.word.descriptor.target.inventory

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.ContainerTargetable
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.InventoryEntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.target.TargetDescriptor
import net.minecraft.world.Container

class InventoryDescriptor: Descriptor(), TargetDescriptor {
  @Override
  override fun modifyTargets(originalTargetables: List<Targetable>, owner: Targetable): List<Targetable> {
      val ownerEntity = if (owner is EntityTargetable) owner.targetEntity else null
      return originalTargetables.stream().map { targetable: Targetable ->
          if (targetable is EntityTargetable)
              return@map InventoryEntityTargetable(
                  targetable.targetEntity,
                  0,
                  targetable.targetEntity === ownerEntity
              )
          if (targetable.level.getBlockEntity(targetable.offsetBlockPos) is Container)
              return@map ContainerTargetable(
                  targetable.level,
                  targetable.offsetBlockPos,
                  0
              )
          targetable
      }.toList()
  }

  override fun replacesSubjectCost() = false
  override fun cost() = Cost(0.0, COSTTYPE.ADDITIVE)
}
