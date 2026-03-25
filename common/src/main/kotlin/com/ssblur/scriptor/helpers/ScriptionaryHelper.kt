package com.ssblur.scriptor.helpers

import com.ssblur.scriptor.data.saved_data.DictionarySavedData
import com.ssblur.scriptor.data.saved_data.PlayerScriptionarySavedData
import com.ssblur.scriptor.network.client.ScriptorNetworkS2C
import com.ssblur.scriptor.word.Spell
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player

object ScriptionaryHelper {
  val PLAYER_NOTES = mutableListOf<String>()
  val PLAYER_OBSERVATIONS = mutableListOf<Pair<String, String>>()

  /**
   * Awards a specific note, with an accompanying message depending on if they have a Scriptionary.
   * TODO
   * @param player The player to award this to
   * @param note The location of the markdown file (minus locale dir) of the note to award.
   */
  fun awardNote(player: Player, note: ResourceLocation) {
    // TODO
  }

  /**
   * Used to record a spell or word when a player discovers it in world.
   * For things like Evokers and Villagers casting spells near players.
   * @param player The player to award this to
   * @param spell The spell as spoken
   */
  fun recordSpell(player: Player, spell: String, component: String? = null) {
    if(player.level().isClientSide) return
    if(PlayerScriptionarySavedData.computeIfAbsent(player)?.observations?.any {
      it.first.trim() == spell.trim()
    } == true)
      return
    PlayerScriptionarySavedData.computeIfAbsent(player)?.let {
      it.observations.add(Pair(spell, component ?: ""))
      it.setDirty()
    }
    ScriptorNetworkS2C.scriptionaryData(
      ScriptorNetworkS2C.ScriptionaryData(2, spell, component ?: "???"),
      listOf(player)
    )
  }

  /**
   * Used to record a spell or word when a player discovers it in world.
   * For things like Evokers and Villagers casting spells near players.
   * @param player The player to award this to
   * @param spell The spell object
   */
  fun recordSpell(player: Player, spell: Spell, component: String? = null) {
    if(player.level().isClientSide) return
    recordSpell(
      player,
      DictionarySavedData.computeIfAbsent(player.level() as ServerLevel).generate(spell).string.trim(),
      component
    )
  }
}