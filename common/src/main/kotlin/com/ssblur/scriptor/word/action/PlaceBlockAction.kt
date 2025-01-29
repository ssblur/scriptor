package com.ssblur.scriptor.word.action

import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.color.CustomColors.getColor
import com.ssblur.scriptor.helpers.ItemTargetableHelper
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.mixin.BlockPlaceContextAccessor
import com.ssblur.scriptor.network.client.ParticleNetwork
import com.ssblur.scriptor.registry.colorable.ColorableBlockRegistry.DYE_COLORABLE_BLOCKS
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.BlockItem
import net.minecraft.world.phys.BlockHitResult

class PlaceBlockAction : Action() {
    override fun cost(): Cost {
        return Cost(1.5, COSTTYPE.ADDITIVE)
    }

    override fun apply(caster: Targetable, targetable: Targetable, descriptors: Array<Descriptor>) {
        val color = getColor(descriptors)
        val pos = targetable.targetBlockPos
        val level = targetable.level

        if (!level.getBlockState(pos).canBeReplaced()) return

        val itemFocus = ItemTargetableHelper.getTargetItemStack(
            caster,
            false
        ) { it.item is BlockItem }

        if (itemFocus.item is BlockItem) {
            val status: InteractionResult = (itemFocus.item as BlockItem).place(
                BlockPlaceContextAccessor.createBlockPlaceContext(
                    level,
                    null,
                    InteractionHand.MAIN_HAND,
                    itemFocus,
                    BlockHitResult(targetable.targetPos, targetable.facing, targetable.targetBlockPos, false)
                )
            )
            if (!status.consumesAction()) ParticleNetwork.fizzle(level, targetable.targetBlockPos)
        } else {
            DYE_COLORABLE_BLOCKS.MAGIC_BLOCK.setColor(color, level, pos)
        }
    }
}
