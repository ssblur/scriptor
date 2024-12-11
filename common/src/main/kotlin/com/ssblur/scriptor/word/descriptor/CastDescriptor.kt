package com.ssblur.scriptor.word.descriptor

import com.ssblur.scriptor.helpers.targetable.Targetable

interface CastDescriptor {
    fun cannotCast(caster: Targetable?): Boolean
}
