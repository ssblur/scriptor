package com.ssblur.scriptor.word.action.potions

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.blockentity.PhasedBlockBlockEntity.Companion.phase
import com.ssblur.scriptor.effect.ScriptorEffects.WILD_PHASING
import com.ssblur.scriptor.helpers.targetable.Targetable

class WildPhasingAction: PotionAction(WILD_PHASING.ref(), 5.0, 1.0 / 3.0, Cost(15.0, COSTTYPE.ADDITIVE)) {
  override fun applyToPosition(
    caster: Targetable?,
    targetable: Targetable?,
    descriptors: Array<Descriptor>?,
    strength: Double,
    duration: Double
  ) {
    val level = targetable!!.level
    val pos = targetable.offsetBlockPos

    phase(level, pos)
  }
}
