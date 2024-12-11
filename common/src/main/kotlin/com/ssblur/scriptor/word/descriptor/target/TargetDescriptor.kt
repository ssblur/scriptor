package com.ssblur.scriptor.word.descriptor.target

import com.ssblur.scriptor.helpers.targetable.Targetable

interface TargetDescriptor {
    fun modifyTargets(originalTargetables: List<Targetable>, owner: Targetable): List<Targetable>
    fun replacesSubjectCost(): Boolean
}
