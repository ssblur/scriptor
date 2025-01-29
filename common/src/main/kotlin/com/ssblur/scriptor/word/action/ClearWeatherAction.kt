package com.ssblur.scriptor.word.action

import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.power.StrengthDescriptor
import net.minecraft.server.level.ServerLevel

class ClearWeatherAction : Action() {
    override fun apply(caster: Targetable, targetable: Targetable, descriptors: Array<Descriptor>) {
        if (targetable.level.isClientSide) return
        var strength = 2.0
        for (d in descriptors) {
            if (d is StrengthDescriptor) strength += d.strengthModifier()
        }

        val serverLevel = targetable.level as ServerLevel
        serverLevel.setWeatherParameters(strength.toInt() * 2000, 0, false, false)
    }

    override fun cost() = Cost(51.0, COSTTYPE.ADDITIVE)
}
