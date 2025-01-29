package com.ssblur.scriptor.word.action

import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.ItemTargetableHelper
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.ItemTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.duration.DurationDescriptor
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.BaseFireBlock

class InflameAction : Action() {
    override fun apply(caster: Targetable, targetable: Targetable, descriptors: Array<Descriptor>) {
        var seconds = 2.0
        for (d in descriptors) {
            if (d is DurationDescriptor) seconds += d.durationModifier()
        }

        if (targetable is ItemTargetable && targetable.shouldTargetItem()) {
            val check = RecipeManager.createCheck(RecipeType.SMELTING)
            val recipe = check.getRecipeFor(SingleRecipeInput(targetable.targetItem), targetable.level)
            if (recipe.isPresent && !recipe.get().value().ingredients.isEmpty() && recipe.get()
                    .value().ingredients[0].items.size > 0
            ) {
                val count = recipe.get().value().ingredients[0].items[0].count
                targetable.targetItem.shrink(count)

                val pos = targetable.targetPos
                val entity = ItemEntity(
                    targetable.level,
                    pos.x(),
                    pos.y() + 1,
                    pos.z(),
                    recipe.get().value().getResultItem(targetable.level.registryAccess())
                )
                caster.level.addFreshEntity(entity)
            }
            return
        }

        val itemTarget = ItemTargetableHelper.getTargetItemStack(targetable)
        if (!itemTarget.isEmpty) {
            val check = RecipeManager.createCheck(RecipeType.SMELTING)
            val recipe = check.getRecipeFor(SingleRecipeInput(itemTarget), targetable.level)
            if (recipe.isPresent && recipe.get().value().ingredients.size > 0 && recipe.get()
                    .value().ingredients[0].items.size > 0
            ) {
                val count = recipe.get().value().ingredients[0].items[0].count
                itemTarget.shrink(count)
                ItemTargetableHelper.depositItemStack(
                    targetable,
                    recipe.get().value().getResultItem(targetable.level.registryAccess())
                )
                return
            }
        }

        if (targetable is EntityTargetable) {
            targetable.targetEntity.remainingFireTicks = Math.round(seconds * 20).toInt()
        } else {
            val pos = targetable.targetBlockPos
            val level = targetable.level

            if (!level.getBlockState(pos).canBeReplaced()) return

            val blockState2 = BaseFireBlock.getState(level, pos)
            level.setBlock(pos, blockState2, 11)

            if (caster is EntityTargetable && caster.targetEntity is Player) level.playSound(
                null,
                pos,
                SoundEvents.FLINTANDSTEEL_USE,
                SoundSource.BLOCKS,
                1.0f,
                level.getRandom().nextFloat() * 0.4f + 0.8f
            )
            else level.playSound(
                null,
                pos,
                SoundEvents.FLINTANDSTEEL_USE,
                SoundSource.BLOCKS,
                1.0f,
                level.getRandom().nextFloat() * 0.4f + 0.8f
            )
        }
    }

    override fun cost(): Cost {
        return Cost(2.0, COSTTYPE.ADDITIVE)
    }
}
