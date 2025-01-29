package com.ssblur.scriptor.word.action.potions

import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.duration.DurationDescriptor
import com.ssblur.scriptor.word.descriptor.power.StrengthDescriptor
import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import kotlin.math.floor
import kotlin.math.max

open class PotionAction(
    var mobEffect: Holder<MobEffect>,
    var durationScale: Double,
    var strengthScale: Double,
    var cost: Cost
) : Action() {
    override fun apply(caster: Targetable, targetable: Targetable, descriptors: Array<Descriptor>) {
        var strength = 0.0
        var duration = 2.0
        for (d in descriptors) {
            if (d is StrengthDescriptor) strength += d.strengthModifier()
            if (d is DurationDescriptor) duration += d.durationModifier()
        }

        strength = max(strength, 0.0)
        strength *= strengthScale
        duration *= durationScale

        // Maybe add poison-tipped enchant?
        if (targetable is EntityTargetable && targetable.targetEntity is LivingEntity) (targetable.targetEntity as LivingEntity).addEffect(
            MobEffectInstance(
                mobEffect, Math.round(duration).toInt(), floor(strength).toInt()
            )
        )
        else applyToPosition(caster, targetable, descriptors, strength, duration)
    }

    open fun applyToPosition(
        caster: Targetable?,
        targetable: Targetable?,
        descriptors: Array<Descriptor>?,
        strength: Double,
        duration: Double
    ) {
    }

    override fun cost(): Cost {
        return this.cost
    }
}
