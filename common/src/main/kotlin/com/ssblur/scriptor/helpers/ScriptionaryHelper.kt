package com.ssblur.scriptor.helpers

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.data.saved_data.DictionarySavedData
import com.ssblur.scriptor.data.saved_data.PlayerScriptionarySavedData
import com.ssblur.scriptor.helpers.ScriptionaryHelper.awardPlayerNote
import com.ssblur.scriptor.helpers.ScriptionaryHelper.getPlayerNotes
import com.ssblur.scriptor.item.ScriptorItems
import com.ssblur.scriptor.network.client.ScriptorNetworkS2C
import com.ssblur.scriptor.word.Spell
import com.ssblur.unfocused.extension.ItemStackExtension.matches
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player

@Suppress("unused")
object ScriptionaryHelper {
  val PLAYER_NOTES = mutableListOf<String>()
  val PLAYER_OBSERVATIONS = mutableListOf<Pair<String, String>>()

  /**
   * Awards a specific note, with an accompanying message depending on if they have a Scriptionary.
   * @param player The player to award this to
   * @param note The location of the markdown file (minus locale dir) of the note to award.
   */
  fun awardPlayerNote(player: Player, note: ResourceLocation) {
    if(player.level().isClientSide) return
    val location = note.toString()
    if(player.hasNote(note)) return

    if(player.inventory.contains { it matches ScriptorItems.DICTIONARY.get() })
      player.sendSystemMessage(Component.translatable("extra.scriptor.record_scriptionary")
        .withStyle(ChatFormatting.GRAY)
        .withStyle(ChatFormatting.ITALIC)
      )
    else
      player.sendSystemMessage(Component.translatable("extra.scriptor.record_no_scriptionary")
        .withStyle(ChatFormatting.GRAY)
        .withStyle(ChatFormatting.ITALIC)
      )

    PlayerScriptionarySavedData.computeIfAbsent(player)?.let {
      it.unlocks.add(location)
      it.setDirty()
    }
    ScriptorNetworkS2C.scriptionaryData(
      ScriptorNetworkS2C.ScriptionaryData(1, location, null),
      listOf(player)
    )
  }

  fun awardPlayerNote(player: Player, note: String) = awardPlayerNote(player, ScriptorMod.location(note))

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
    player.awardNote(ScriptorMod.location("oddities/entity_casting"))
    recordSpell(
      player,
      DictionarySavedData.computeIfAbsent(player.level() as ServerLevel).generate(spell).string.trim(),
      component
    )
  }

  /**
   * Gets the notes already unlocked by a player
   * @param player The player to get notes for
   */
  fun getPlayerNotes(player: Player): List<String> {
    if(player.level().isClientSide) return PLAYER_NOTES
    return PlayerScriptionarySavedData.computeIfAbsent(player)?.unlocks ?: listOf()
  }

  /**
   * @see getPlayerNotes
   */
  val Player.notes: List<ResourceLocation>
    get() = getPlayerNotes(this).map { ScriptorMod.location(it) }

  /**
   * @see awardPlayerNote
   */
  fun Player.awardNote(note: ResourceLocation) = awardPlayerNote(this, note)
  fun Player.hasNote(note: ResourceLocation) = this.notes.contains(note)
}