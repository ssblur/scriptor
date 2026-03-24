package com.ssblur.scriptor.api.word

import com.ssblur.scriptor.helpers.targetable.Targetable

abstract class Action: Word() {
  /**
   * Applies the effects of this spell.
   * This step should factor in any Descriptors on this spell.
   * @param caster The Entity which cast this spell
   * @param targetable A Targetable which describes the target of this spell (position, entity, item, etc.)
   * @param descriptors A list of all Descriptors which this spell contains
   * @param spellData A list of data collected for this spell, in parse order
   */
  abstract fun apply(caster: Targetable, targetable: Targetable, descriptors: Array<Descriptor>, spellData: MutableList<String>)
}
