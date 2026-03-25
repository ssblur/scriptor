package com.ssblur.scriptor.resources

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.helpers.ScriptionaryHelper.notes
import com.ssblur.unfocused.data.DataLoaderRegistry.registerSimpleDataLoader
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player

object Notes {
  data class Note(var keys: List<ResourceLocation>, var tier: Int = 0)

  val notes = ScriptorMod.registerSimpleDataLoader("scriptor/notes", Note::class)

  fun getRandomNote(t: Int, player: Player): ResourceLocation =
    notes.values.filter { it.tier == t }.flatMap { it.keys }.filter { it !in player.notes }.randomOrNull()
      ?: notes.values.filter { it.tier == t }.random().keys.random()
}