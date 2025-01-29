package com.ssblur.scriptor.resources

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.error.WordNotFoundException
import com.ssblur.scriptor.registry.words.WordRegistry.actionRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.descriptorRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.subjectRegistry
import com.ssblur.scriptor.word.PartialSpell
import com.ssblur.scriptor.word.Spell
import com.ssblur.unfocused.data.DataLoaderRegistry.registerSimpleDataLoader

object Engravings {
    class EngravingResource {
        class PartialSpellResource(var action: String, var descriptors: List<String>)
        class SpellResource(var subject: String, var spells: List<PartialSpellResource>)

        var spell: SpellResource? = null

        fun generateSpell(): Spell {
            val spells = mutableListOf<PartialSpell>()
            for (spell in spell!!.spells) {
                val action = actionRegistry[spell.action] ?: throw WordNotFoundException(spell.action)
                val descriptors = spell.descriptors.map{ descriptorRegistry[it] ?: throw WordNotFoundException(it) }
                spells += PartialSpell(action, *descriptors.toTypedArray())
            }
            return Spell(subjectRegistry[spell!!.subject] ?: throw WordNotFoundException(spell!!.subject), *spells.toTypedArray())
        }
    }
    val engravings = ScriptorMod.registerSimpleDataLoader("scriptor/engravings", EngravingResource::class)
    fun getRandomEngraving() = engravings.entries.random().value
}