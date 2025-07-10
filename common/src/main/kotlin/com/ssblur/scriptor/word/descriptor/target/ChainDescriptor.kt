package com.ssblur.scriptor.word.descriptor.target

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.network.client.ParticleNetwork
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

class ChainDescriptor: Descriptor(), TargetDescriptor {
  override fun modifyTargets(originalTargetables: List<Targetable>, owner: Targetable): List<Targetable> {
    val targetables = originalTargetables.toMutableList()
    if (targetables.isEmpty()) return targetables

    if (targetables.findLast { it is EntityTargetable } != null) {
      val entityTargetable = targetables.findLast { it is EntityTargetable } as EntityTargetable
      val pos: Vec3 = entityTargetable.targetPos
      val range = 5.0
      val entities: List<LivingEntity> = entityTargetable.level.getEntitiesOfClass(
        LivingEntity::class.java,
        AABB.ofSize(pos, range, 1.5, range)
      )
      if (entities.size > 1) {
        val filteredEntities = entities.filter { ent ->
          targetables.none{ (it is EntityTargetable && it.targetEntity.`is`(ent)) }
        }.filter { ent ->
          owner !is EntityTargetable || !owner.targetEntity.`is`(ent)
        }
        if(filteredEntities.isNotEmpty()) {
          val target = filteredEntities[0]
          targetables.add(EntityTargetable(target))
          ParticleNetwork.magicTrail(target.level(), 0xffffff, target.eyePosition, entityTargetable.targetEntity.eyePosition)
        }
      }
      return targetables
    }

    val firstValidTarget = originalTargetables.firstNotNullOfOrNull {
      chooseAdjacent(it, originalTargetables)
    }
    if(firstValidTarget != null)
      return originalTargetables + listOf(firstValidTarget)
    return originalTargetables
  }

  override fun replacesSubjectCost() = false
  override fun allowsDuplicates() = true
  override fun cost() = Cost(1.25, COSTTYPE.MULTIPLICATIVE)

  companion object {
    fun chooseAdjacent(targetable: Targetable, targetables: List<Targetable>): Targetable? {
      val block = targetable.level.getBlockState(targetable.targetBlockPos).block
      return listOf(
        targetable.targetBlockPos.offset(1, 0, 0),
        targetable.targetBlockPos.offset(-1, 0, 0),
        targetable.targetBlockPos.offset(0, 1, 0),
        targetable.targetBlockPos.offset(0, -1, 0),
        targetable.targetBlockPos.offset(0, 0, 1),
        targetable.targetBlockPos.offset(0, 0, -1),
      )
        .filter { pos -> targetables.none { it.targetBlockPos == pos } }
        .filter { targetable.level.getBlockState(it).`is`(block) }
        .firstNotNullOfOrNull { Targetable(targetable.level, it) }
    }
  }
}
