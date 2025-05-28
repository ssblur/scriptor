package com.ssblur.scriptor.helpers.targetable

import net.minecraft.world.entity.Entity

open class EntityTargetable(var targetEntity: Entity): Targetable(targetEntity.level(), targetEntity.position()) {
    override val entityYRotation: Float? = if (targetEntity.getYRot() > 0) {
        targetEntity.getYRot()
    } else {
        targetEntity.getYRot() + 360
    }

    override val entityCoarseYRotation: Float? = when (entityYRotation!!.toInt()) {
        in 0..45 -> 0f // South
        in 315..360 -> 0f // South
        in 45..135 -> 90f // East
        in 135..225 -> 180f // North
        else -> 270f // West
    }
}
