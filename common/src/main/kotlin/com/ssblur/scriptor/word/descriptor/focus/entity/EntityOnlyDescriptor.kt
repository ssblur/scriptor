package com.ssblur.scriptor.word.descriptor.focus.entity

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.focus.MultiTargetFocusDescriptor
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import com.ssblur.scriptor.network.client.ParticleNetwork

/**
* Get all entities in the area described by the current targeted non-entity locations. If no entities are in the
 * area this deletes all targets - this is useful for making some spells 'safe', e.g. using ignite to ignite a mob
 * but missing and hitting your wooden house.
 */
class EntityOnlyDescriptor: Descriptor(), MultiTargetFocusDescriptor {
  override fun modifyTargetsFocus(originalTargetables: List<Targetable>, owner: Targetable): List<Targetable> {
    val targetables = mutableListOf<Targetable>()
    if (originalTargetables.isEmpty()) return targetables

    for (originalTargetable in originalTargetables) {
      if (originalTargetable is EntityTargetable) {
        targetables.add(originalTargetable)
      } else {
        val pos: Vec3 = originalTargetable.targetPos
        val range = 1.5
        val entities: List<LivingEntity> = originalTargetable.level.getEntitiesOfClass(
          LivingEntity::class.java,
          AABB.ofSize(pos, range, range, range)
        )
        if (entities.size > 0) {
          val filteredEntities = entities.filter { ent ->
            originalTargetables.none{ (it is EntityTargetable && it.targetEntity.`is`(ent)) }
          }.filter { ent ->
            targetables.none{ (it is EntityTargetable && it.targetEntity.`is`(ent)) }
          }
          if(filteredEntities.isNotEmpty()) {
            //        Set limit to number of entities captured per-block to avoid performance problems with crammed entities
            val limitedFilteredEntities: List<Targetable> = filteredEntities.map{EntityTargetable(it)}.take(3)
            if (targetables.isNotEmpty()) {
              val entityTargetable: EntityTargetable = targetables.last() as EntityTargetable
              ParticleNetwork.magicTrail(filteredEntities.first().level(), 0xffffff, entityTargetable.targetEntity.eyePosition, filteredEntities.first().eyePosition)
            }
            targetables.addAll(limitedFilteredEntities)
          }
        }
      }
    }
    return targetables
  }

  override fun replacesSubjectCost() = false
  override fun allowsDuplicates() = true
  override fun cost() = Cost(1.25, COSTTYPE.MULTIPLICATIVE)

}


