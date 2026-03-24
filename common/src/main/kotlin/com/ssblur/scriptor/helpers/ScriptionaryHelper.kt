package com.ssblur.scriptor.helpers

import com.ssblur.scriptor.data.saved_data.DictionarySavedData
import com.ssblur.scriptor.word.Spell
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player

object ScriptionaryHelper {

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
  fun recordSpell(player: Player, spell: String) {
    // TODO
  }

  /**
   * Used to record a spell or word when a player discovers it in world.
   * For things like Evokers and Villagers casting spells near players.
   * @param player The player to award this to
   * @param spell The spell object
   */
  fun recordSpell(player: Player, spell: Spell) {
    if(player.level().isClientSide) return
    recordSpell(player, DictionarySavedData.computeIfAbsent(player.level() as ServerLevel).generate(spell).string)
  }
}