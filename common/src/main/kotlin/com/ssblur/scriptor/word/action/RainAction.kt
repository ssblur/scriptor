package com.ssblur.scriptor.word.action

import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.power.StrengthDescriptor
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LightningBolt
import kotlin.math.sign

class RainAction : Action() {
    override fun apply(caster: Targetable, targetable: Targetable, descriptors: Array<Descriptor>) {
        if (targetable.level.isClientSide) return
        var strength = 2.0
        for (d in descriptors) {
            if (d is StrengthDescriptor) strength += d.strengthModifier()
        }

        val serverLevel = targetable.level as ServerLevel
        serverLevel.setWeatherParameters(0, strength.toInt() * 2000, true, strength > 6)

        strength -= 9.0
        var i = strength
        while (i >= 0) {
            var x = Math.random() * 12 - 6
            val y = Math.random() * -1
            var z = Math.random() * 12 - 6
            x += sign(x) * 2
            z += sign(z) * 2
            val bolt = LightningBolt(EntityType.LIGHTNING_BOLT, serverLevel)
            if (caster is EntityTargetable && caster.targetEntity is ServerPlayer) bolt.cause = (caster.targetEntity as ServerPlayer)
            bolt.setPos(targetable.targetPos.add(x, y, z))
            serverLevel.addFreshEntity(bolt)
            i -= 5.0
        }
    }

    override fun cost() = Cost(51.0, COSTTYPE.ADDITIVE)
}
