package com.ssblur.scriptor.resources

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.resources.shared.SpellResource
import com.ssblur.scriptor.word.Spell
import com.ssblur.unfocused.data.DataLoaderRegistry.registerSimpleDataLoader

object VillagerEngravings {
  class EngravingResource {
    var spell: SpellResource? = null

    fun generateSpell(): Spell = spell!!.getSpell()
  }

  val engravings = ScriptorMod.registerSimpleDataLoader("scriptor/villager_engravings", EngravingResource::class)
  fun getRandomEngraving() = engravings.entries.random().value
}