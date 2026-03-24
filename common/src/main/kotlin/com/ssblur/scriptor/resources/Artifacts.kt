package com.ssblur.scriptor.resources

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.data.saved_data.DictionarySavedData.Companion.computeIfAbsent
import com.ssblur.scriptor.resources.shared.SpellResource
import com.ssblur.scriptor.word.Spell
import com.ssblur.unfocused.data.DataLoaderRegistry.registerSimpleDataLoader
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack

object Artifacts {
  class ArtifactResource {
    var name: String? = null
    var spell: SpellResource? = null

    fun getSpell(): Spell = spell!!.getSpell()

    fun applyToItem(itemStack: ItemStack, level: ServerLevel): ItemStack {
      itemStack[ScriptorDataComponents.SPELL] = computeIfAbsent(level).generate(this.getSpell()).string
      itemStack[DataComponents.ITEM_NAME] = Component.translatable(name ?: "")
      return itemStack
    }
  }

  val artifacts = ScriptorMod.registerSimpleDataLoader("scriptor/artifacts", ArtifactResource::class)
  fun getRandomArtifact() = artifacts.entries.random().value
}