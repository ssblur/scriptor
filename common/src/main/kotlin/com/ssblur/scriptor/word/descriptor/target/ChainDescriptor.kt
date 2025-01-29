package com.ssblur.scriptor.word.descriptor.target

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.MathHelper
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import net.minecraft.core.Direction
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import java.util.*

class ChainDescriptor : Descriptor(), TargetDescriptor {
    override fun cost(): Cost {
        return Cost(1.25, COSTTYPE.MULTIPLICATIVE)
    }

    override fun modifyTargets(originalTargetables: List<Targetable>, owner: Targetable): List<Targetable> {
        var targetables = originalTargetables
        if (targetables.isEmpty()) return targetables
        targetables = ArrayList(targetables)

        val random = Random()
        if (targetables[random.nextInt(targetables.size)] is EntityTargetable) {
            val entityTargetable = targetables[random.nextInt(targetables.size)] as EntityTargetable
            val pos: Vec3 = entityTargetable.targetPos
            val entities: List<LivingEntity> = entityTargetable.level.getEntitiesOfClass<LivingEntity>(
                LivingEntity::class.java,
                AABB.ofSize(
                    Vec3(
                        pos.x() - 1,
                        pos.y() - 1,
                        pos.z() - 1
                    ),
                    3.0,
                    3.0,
                    3.0
                )
            )
            if (entities.size > 1) {
                var newTarget: LivingEntity
                do newTarget = entities[random.nextInt(entities.size)]
                while (newTarget !== entityTargetable.targetEntity)
                targetables.add(EntityTargetable(newTarget))
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

    override fun replacesSubjectCost(): Boolean {
        return false
    }

    override fun allowsDuplicates(): Boolean {
        return true
    }
}
