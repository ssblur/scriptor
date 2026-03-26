package com.ssblur.scriptor.api

import net.minecraft.world.entity.player.Player
import com.ssblur.scriptor.extension.EntityCastCooldownExtension.castCooldown as cc

@Suppress("unused")
object ScriptorPlayerData {
  val Player.castCooldown: Long
    get() = this.cc
}