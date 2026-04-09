package com.ssblur.scriptor.extension

import com.ssblur.scriptor.ScriptorMod.MANA_MODE
import com.ssblur.scriptor.mixin.MinecraftClientTickAccessor
import com.ssblur.scriptor.network.client.ScriptorNetworkS2C
import com.ssblur.scriptor.word.Spell
import com.ssblur.scriptor.word.subject.InventorySubject
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import java.util.*
import kotlin.math.min

object EntityCastCooldownExtension {
  private val CACHE: WeakHashMap<Entity, Long> = WeakHashMap()
  private var LOCAL_COUNT = 0L
  const val COOLDOWN_MULT = 0.2

  private val MANA: WeakHashMap<Player, Double> = WeakHashMap()
  const val DEFAULT_MANA = 5000.0

  var Entity.castCooldown: Long
    get() {
      if(MANA_MODE && this is Player)
        return if(this.mana > 0) 0 else 1

      val level = this.level()
      if(level is ServerLevel)
        return CACHE[this]?.let { it - level.server.tickCount.toLong() } ?: 0L
      else if(level is ClientLevel)
        if(this == Minecraft.getInstance().player)
          return LOCAL_COUNT - (Minecraft.getInstance() as MinecraftClientTickAccessor).clientTickCount
      return 0
    }
    set(value) {
      if(MANA_MODE && this is Player) {
        MANA[this] = this.mana - value.toDouble()
      }

      val cooldown = (value * COOLDOWN_MULT).toLong()
      val level = this.level()
      if(level is ServerLevel) {
        if(this is ServerPlayer)
          ScriptorNetworkS2C.cooldown(ScriptorNetworkS2C.Cooldown(value), listOf(this))
        CACHE[this] = cooldown + level.server.tickCount
      } else if(this == Minecraft.getInstance().player)
        LOCAL_COUNT = cooldown + (Minecraft.getInstance() as MinecraftClientTickAccessor).clientTickCount
    }

  fun Entity.canCast(spell: Spell, mult: Double = 1.0): Boolean {
    if(MANA_MODE && this is Player) {
      if(spell.subject is InventorySubject) return true
      return this.mana >= (spell.cost() * mult)
    }
    return this.castCooldown <= 0
  }

  var Player.mana: Double
    get() = MANA[this] ?: this.maxMana
    set(value) {
      MANA[this] = min(value, this.maxMana)
      if(!this.level().isClientSide && value != MANA[this])
        ScriptorNetworkS2C.mana(ScriptorNetworkS2C.Mana(value), listOf(this))
    }

  val Entity.maxMana: Double
    get() = DEFAULT_MANA
}