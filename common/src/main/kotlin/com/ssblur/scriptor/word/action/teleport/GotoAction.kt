package com.ssblur.scriptor.word.action.teleport

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.Targetable

class GotoAction : SwapAction() {
    override fun apply(caster: Targetable, targetable: Targetable, descriptors: Array<Descriptor>) {
        if (targetable.level.isClientSide) return

        teleport(caster, targetable)
    }

    override fun cost() = Cost(6.0, COSTTYPE.ADDITIVE)
}
