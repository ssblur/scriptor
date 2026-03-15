package com.ssblur.scriptor.helpers

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player

object ScriptionaryHelper {

  /**
   * Awards a specific note, with an accompanying message depending on if they have a Scriptionary.
   * TODO
   * @param player The player to award this to
   * @param note The location of the markdown file (minus locale dir) of the note to award.
   */
  fun awardNote(player: Player, note: ResourceLocation) {
    throw NotImplementedError()
  }
}