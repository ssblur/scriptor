package com.ssblur.scriptor.resources

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.ScriptorMod.COMMUNITY_MODE
import com.ssblur.scriptor.advancement.ScriptorAdvancements.TOME
import com.ssblur.scriptor.advancement.ScriptorAdvancements.TOME_1
import com.ssblur.scriptor.advancement.ScriptorAdvancements.TOME_2
import com.ssblur.scriptor.advancement.ScriptorAdvancements.TOME_3
import com.ssblur.scriptor.advancement.ScriptorAdvancements.TOME_4
import com.ssblur.scriptor.data.saved_data.PlayerSpellsSavedData.Companion.computeIfAbsent
import com.ssblur.scriptor.error.WordNotFoundException
import com.ssblur.scriptor.registry.words.WordRegistry.actionRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.descriptorRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.subjectRegistry
import com.ssblur.scriptor.word.PartialSpell
import com.ssblur.scriptor.word.Spell
import com.ssblur.unfocused.data.DataLoaderRegistry.registerSimpleDataLoader
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import kotlin.math.min
import kotlin.random.Random

object Tomes {
  class TomeResource {
    class PartialSpellResource(var action: String, var descriptors: List<String>)
    class SpellResource(var subject: String, var spells: List<PartialSpellResource>)

    var name: String = "items.scriptor.spellbook"
    var author: String? = null
    var spell: SpellResource? = null
    var item: String? = null
    var tier: Int = 0

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
  }

  val tomes = ScriptorMod.registerSimpleDataLoader("scriptor/tomes", TomeResource::class)
  val random = Random(System.nanoTime())
  fun tier(tier: Int) = tomes.entries.filter { it.value.tier == tier }.toTypedArray()

  fun getRandomTome(t: Int, player: Player): TomeResource {
    var options = tier(t)
    if (COMMUNITY_MODE) {
      val level = player.level()
      if (level is ServerLevel) {
        var bracket: Int = level.seed.toInt() % 5
        bracket = min((bracket + 10) % 5, (options.size - 1))

        val filtered: MutableList<MutableMap.MutableEntry<ResourceLocation, TomeResource>> = mutableListOf()
        for (i in options.indices) if (bracket == i % 5) filtered.add(options[i])
        options = filtered.toTypedArray()
      }
    }

    val data = computeIfAbsent(player)
    if (data != null) {
      val known = data.getTier(t)
      val remaining = options.filter { entry -> known.none{ (key, _) -> key == entry.key.toShortLanguageKey() } }
      println(remaining)

      TOME.get().trigger(player as ServerPlayer)
      if(remaining.size <= 1){
        if (t == 1) TOME_1.get().trigger(player)
        if (t == 2) TOME_2.get().trigger(player)
        if (t == 3) TOME_3.get().trigger(player)
        if (t == 4) TOME_4.get().trigger(player)
      }

      if(remaining.isEmpty()) {
        return options[random.nextInt(options.size)].value
      }
      val option = remaining.random()
      known[option.key.toShortLanguageKey()] = true
      data.setDirty()
      return option.value
    }
    return options[random.nextInt(options.size)].value
  }
}