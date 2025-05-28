package com.ssblur.scriptor.word.descriptor.target

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.targetable.Targetable


interface GeometricTargetDescriptor {
//    Pass in list of descriptors to allow counting the number of specific descriptors
    fun modifyTargets(originalTargetables: List<Targetable>, owner: Targetable, descriptors: Array<Descriptor>): List<Targetable>
    fun replacesSubjectCost(): Boolean
}
