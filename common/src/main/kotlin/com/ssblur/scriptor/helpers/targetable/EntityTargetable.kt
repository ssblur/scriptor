package com.ssblur.scriptor.helpers.targetable

import net.minecraft.world.entity.Entity

open class EntityTargetable(@JvmField var targetEntity: Entity) : Targetable(targetEntity.level(), targetEntity.position())
