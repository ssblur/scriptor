package com.ssblur.scriptor.resources.shared

import com.ssblur.scriptor.error.WordNotFoundException
import com.ssblur.scriptor.registry.words.WordRegistry.actionRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.descriptorRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.subjectRegistry
import com.ssblur.scriptor.word.PartialSpell
import com.ssblur.scriptor.word.Spell

class SpellResource(var subject: String, var spells: List<PartialSpellResource>, var spellData: List<String>?) {
  class PartialSpellResource(var action: String, var descriptors: List<String>)
  fun getSpell(): Spell {
    val spells = mutableListOf<PartialSpell>()
    for (spell in this.spells) {
      val action = actionRegistry[spell.action] ?: throw WordNotFoundException(spell.action)
      val descriptors = spell.descriptors.map { descriptorRegistry[it] ?: throw WordNotFoundException(it) }
      spells += PartialSpell(action, *descriptors.toTypedArray())
    }
    return Spell(
      subjectRegistry[this.subject] ?: throw WordNotFoundException(this.subject),
      *spells.toTypedArray(),
      spellData = spellData ?: listOf()
    )
  }
}