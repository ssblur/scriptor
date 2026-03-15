package com.ssblur.scriptor.resources

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.data.saved_data.DictionarySavedData.Companion.computeIfAbsent
import com.ssblur.scriptor.error.WordNotFoundException
import com.ssblur.scriptor.registry.words.WordRegistry.actionRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.descriptorRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.subjectRegistry
import com.ssblur.scriptor.word.PartialSpell
import com.ssblur.scriptor.word.Spell
import com.ssblur.unfocused.data.DataLoaderRegistry.registerSimpleDataLoader
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack

object Artifacts {
  class ArtifactResource {
    class PartialSpellResource(var action: String, var descriptors: List<String>)
    class SpellResource(var subject: String, var spells: List<PartialSpellResource>)

    var name: String? = null
    var spell: SpellResource? = null

    fun getSpell(): Spell {
      val spells = mutableListOf<PartialSpell>()
      for (spell in spell!!.spells) {
        val action = actionRegistry[spell.action] ?: throw WordNotFoundException(spell.action)
        val descriptors = spell.descriptors.map { descriptorRegistry[it] ?: throw WordNotFoundException(it) }
        spells += PartialSpell(action, *descriptors.toTypedArray())
      }
      return Spell(
        subjectRegistry[spell!!.subject] ?: throw WordNotFoundException(spell!!.subject),
        *spells.toTypedArray()
      )
    }

    fun applyToItem(itemStack: ItemStack, level: ServerLevel): ItemStack {
      itemStack[ScriptorDataComponents.SPELL] = computeIfAbsent(level).generate(this.getSpell())
      itemStack[DataComponents.ITEM_NAME] = Component.translatable(name ?: "")
      return itemStack
    }
  }

  val artifacts = ScriptorMod.registerSimpleDataLoader("scriptor/artifacts", ArtifactResource::class)
  fun getRandomArtifact() = artifacts.entries.random().value
}