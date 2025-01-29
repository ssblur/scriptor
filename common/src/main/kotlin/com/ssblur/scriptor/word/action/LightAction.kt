package com.ssblur.scriptor.word.action

import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.block.ScriptorBlocks
import com.ssblur.scriptor.blockentity.LightBlockEntity
import com.ssblur.scriptor.color.CustomColors.getColor
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.duration.DurationDescriptor
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity

class LightAction : Action() {
    override fun apply(caster: Targetable, targetable: Targetable, descriptors: Array<Descriptor>) {
        var seconds = 6
        for (d in descriptors) {
            if (d is DurationDescriptor) seconds = (seconds + 3 * d.durationModifier()).toInt()
        }

        if (targetable is EntityTargetable && targetable.targetEntity is LivingEntity) {
            (targetable.targetEntity as LivingEntity).addEffect(MobEffectInstance(MobEffects.GLOWING, seconds))
            return
        }

        val color = getColor(descriptors)
        val pos = targetable.targetBlockPos
        val level = targetable.level

        if (!level.getBlockState(pos).canBeReplaced()) return

        val blockState2 = ScriptorBlocks.LIGHT.get().defaultBlockState()
        level.setBlock(pos, blockState2, 11)

        val blockEntity = level.getBlockEntity(pos)
        if (blockEntity is LightBlockEntity) blockEntity.setColor(color)

        level.playSound(
            null,
            pos,
            SoundEvents.WOOL_PLACE,
            SoundSource.BLOCKS,
            1.0f,
            level.getRandom().nextFloat() * 0.4f + 0.8f
        )
    }

    override fun cost(): Cost {
        return Cost(2.0, COSTTYPE.ADDITIVE)
    }
}
