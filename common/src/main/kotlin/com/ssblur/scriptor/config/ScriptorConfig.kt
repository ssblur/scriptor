package com.ssblur.scriptor.config

import com.ssblur.scriptor.ScriptorMod.config

object ScriptorConfig {
  val TOME_MAX_COST = config.registerInt("tome_max_cost", 50)
  val TOME_COOLDOWN_MULTIPLIER = config.registerInt("tome_cooldown_multiplier", 100)
  val SCROLL_MAX_COST = config.registerInt("scroll_max_cost", 75)
  val SCROLL_COOLDOWN_MULTIPLIER = config.registerInt("scroll_cooldown_multiplier", 50)
  val CHALK_MAX_COST = config.registerInt("chalk_max_cost", 250)
  val CASTING_LECTERN_MAX_COST = config.registerInt("casting_lectern_max_cost", 25)
  val CASTING_LECTERN_COOLDOWN_MULTIPLIER = config.registerInt("casting_lectern_cooldown_multiplier", 100)
  val VOCAL_MAX_COST = config.registerInt("vocal_max_cost", -1)
  val VOCAL_HUNGER_THRESHOLD = config.registerInt("vocal_hunger_threshold", 50)
  val VOCAL_DAMAGE_THRESHOLD = config.registerInt("vocal_damage_threshold", 125)
  val VOCAL_COOLDOWN_MULTIPLIER = config.registerInt("vocal_cooldown_multiplier", 100)
  val CAN_TARGET_PLAYER_INVENTORIES = config.registerBoolean("can_target_player_inventories", true)

  val PROXIMITY_CHAT = config.registerBoolean("proximity_chat", false)
  val SHOW_SPELLS_IN_CHAT = config.registerBoolean("show_spells_in_chat", false)
  val PROXIMITY_RANGE = config.registerInt("proximity_chat_range", 64)

  fun register() {}
}