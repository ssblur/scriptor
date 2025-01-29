package com.ssblur.scriptor.word.descriptor.focus

import com.ssblur.scriptor.helpers.targetable.Targetable

interface FocusDescriptor {
    fun modifyFocus(targetable: Targetable): Targetable
}
