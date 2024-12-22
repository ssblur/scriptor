package com.ssblur.scriptor.word.action.potions

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.blockentity.PhasedBlockBlockEntity.Companion.phase
import com.ssblur.scriptor.effect.ScriptorEffects.PHASING
import com.ssblur.scriptor.helpers.targetable.Targetable
import kotlin.math.floor

class PhasingAction : PotionAction(PHASING.ref(), 20.0, 1.0, Cost(8.0, COSTTYPE.ADDITIVE)) {
    override fun applyToPosition(
        caster: Targetable?,
        targetable: Targetable?,
        descriptors: Array<Descriptor>?,
        strength: Double,
        duration: Double
    ) {
        val level = targetable!!.level
        val pos = targetable.offsetBlockPos

        phase(level, pos, floor(duration * 3).toInt())
    }
}
