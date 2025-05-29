package com.ssblur.scriptor.word.descriptor.focus

import com.ssblur.scriptor.helpers.targetable.Targetable

interface MultiTargetFocusDescriptor {
  fun modifyTargetsFocus(originalTargetables: List<Targetable>, owner: Targetable): List<Targetable>
  fun replacesSubjectCost(): Boolean
}
