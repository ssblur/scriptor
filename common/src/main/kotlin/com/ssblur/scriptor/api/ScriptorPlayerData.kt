package com.ssblur.scriptor.api

import com.ssblur.scriptor.word.Spell
import net.minecraft.world.entity.player.Player
import com.ssblur.scriptor.extension.EntityCastCooldownExtension.canCast as canC
import com.ssblur.scriptor.extension.EntityCastCooldownExtension.castCooldown as cc

@Suppress("unused")
object ScriptorPlayerData {
  val Player.castCooldown: Long
    get() = this.cc

  fun Player.canCast(spell: Spell) = this.canC(spell)
}