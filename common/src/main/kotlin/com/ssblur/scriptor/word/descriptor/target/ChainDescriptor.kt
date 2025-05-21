package com.ssblur.scriptor.word.descriptor.target

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.MathHelper
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.network.client.ParticleNetwork
import net.minecraft.core.Direction
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

    val offset = MathHelper.spiral(targetables.size + 1)
    val axis = targetables[0].facing.axis
    val pos = targetables[0].targetPos
    val newPos = if (axis === Direction.Axis.X) Vec3(
      pos.x,
      pos.y + offset.y,
      pos.z + offset.x
    )
    else if (axis === Direction.Axis.Y) Vec3(
      pos.x + offset.x,
      pos.y,
      pos.z + offset.y
    )
    else Vec3(pos.x + offset.x, pos.y + offset.y, pos.z)
    val targetable = targetables[0]
    targetables.add(Targetable(targetable.level, newPos).setFacing(targetable.facing))

    return targetables
  }

  override fun replacesSubjectCost() = false
  override fun allowsDuplicates() = true
  override fun cost() = Cost(1.25, COSTTYPE.MULTIPLICATIVE)

}
