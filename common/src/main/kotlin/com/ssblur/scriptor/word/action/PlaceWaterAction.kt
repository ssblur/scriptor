package com.ssblur.scriptor.word.action

import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.Targetable
import net.minecraft.world.level.block.Blocks

class PlaceWaterAction : Action() {
    override fun cost(): Cost {
        return Cost(1.5, COSTTYPE.ADDITIVE)
    }

    override fun apply(caster: Targetable, targetable: Targetable, descriptors: Array<Descriptor>) {
        val pos = targetable.targetBlockPos
        val level = targetable.level

        if (!level.getBlockState(pos).canBeReplaced()) return
        level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState())
    }
}
