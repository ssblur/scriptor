package com.ssblur.scriptor.extension

import com.ssblur.scriptor.mixin.MinecraftClientTickAccessor
import com.ssblur.scriptor.network.client.ScriptorNetworkS2C
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import java.util.*

object PlayerCastCooldownExtension {
  private val CACHE: WeakHashMap<Entity, Long> = WeakHashMap()
  private var LOCAL_COUNT = 0L

  var Entity.castCooldown: Long
    get() {
      val level = this.level()
      if(level is ServerLevel)
        return CACHE[this]?.let { it - level.server.tickCount.toLong() } ?: 0L
      else if(level is ClientLevel)
        if(this == Minecraft.getInstance().player)
          return LOCAL_COUNT - (Minecraft.getInstance() as MinecraftClientTickAccessor).clientTickCount
      return 0
    }
    set(value) {
      val level = this.level()
      if(level is ServerLevel) {
        if(this is ServerPlayer)
          ScriptorNetworkS2C.cooldown(ScriptorNetworkS2C.Cooldown(value), listOf(this))
        CACHE[this] = value + level.server.tickCount
      } else if(this == Minecraft.getInstance().player)
        LOCAL_COUNT = value + (Minecraft.getInstance() as MinecraftClientTickAccessor).clientTickCount
    }
}