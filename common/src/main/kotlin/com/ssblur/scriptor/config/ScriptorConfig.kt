package com.ssblur.scriptor.config

import com.ssblur.scriptor.ScriptorMod.config

object ScriptorConfig {
  // Casting cost settings
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
  val ITEM_MAX_COST = config.registerInt("other_item_max_cost", 30)
  val ITEM_COOLDOWN_MULTIPLIER = config.registerInt("other_item_cooldown_multiplier", 120)
  val ARTIFACT_COOLDOWN_MULTIPLIER = config.registerInt("artifact_cooldown_multiplier", 200)

  // Spell settings
  val CAN_TARGET_PLAYER_INVENTORIES = config.registerBoolean("can_target_player_inventories", false)

  // Chat settings
  val PROXIMITY_CHAT = config.registerBoolean("proximity_chat", false)
  val SHOW_SPELLS_IN_CHAT = config.registerBoolean("show_spells_in_chat", false)
  val CAST_COMMAND_ENABLED = config.registerBoolean("cast_command_enabled", false)
  val CHAT_CAST_ENABLED = config.registerBoolean("cast_through_chat_enabled", true)
  val PROXIMITY_RANGE = config.registerInt("proximity_chat_range", 64)

  // Other cast settings
  val ENTITY_CASTING = config.registerBoolean("entities_verbally_cast", true)
  val ENTITY_CASTING_PROXIMITY = config.registerInt("entities_verbal_proximity", 16)

  // Demonstration / showcase settings
  val INVERT_DO_NOT_PHASE = config.registerBoolean("invert_do_not_phase", false)

  fun register() {}
}