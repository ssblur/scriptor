package com.ssblur.scriptor.helpers.targetable

import net.minecraft.core.BlockPos
import net.minecraft.world.Container
import net.minecraft.world.level.Level

class ContainerTargetable(level: Level, pos: BlockPos, var slot: Int) : Targetable(level, pos), InventoryTargetable {
    override val container: Container?
        get() {
            if (level.getBlockEntity(targetBlockPos) is Container) return level.getBlockEntity(targetBlockPos) as Container
            return null
        }

    override var targetedSlot = slot
}
