package com.ssblur.scriptor.helpers.resource

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.error.WordNotFoundException
import com.ssblur.scriptor.registry.words.WordRegistry.actionRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.descriptorRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.subjectRegistry
import com.ssblur.scriptor.word.PartialSpell
import com.ssblur.scriptor.word.Spell

class ArtifactResource {
    class PartialSpellResource(var action: String, var descriptors: List<String>)
    class SpellResource(var subject: String, var spells: List<PartialSpellResource>)

    @JvmField
    var name: String? = null
    var isDisabled: Boolean = false
    var spell: SpellResource? = null


    fun getSpell(): Spell {
        val spells: MutableList<PartialSpell> = ArrayList()

        for (spell in spell!!.spells) {
            val action = actionRegistry[spell.action] ?: throw WordNotFoundException(spell.action)

            val descriptors = ArrayList<Descriptor>()
            for (string in spell.descriptors) {
                val descriptor = descriptorRegistry[string] ?: throw WordNotFoundException(string)
                descriptors.add(descriptor)
            }

            spells.add(PartialSpell(action, *descriptors.toTypedArray()))
        }

        val subject = subjectRegistry[spell!!.subject] ?: throw WordNotFoundException(spell!!.subject)

        return Spell(subject, *spells.toTypedArray())
    }
}
