package com.ssblur.scriptor.config

import net.minecraft.world.level.GameRules

object ChatRules {
    var PROXIMITY_CHAT: GameRules.Key<GameRules.BooleanValue>? = null
    var SHOW_SPELLS_IN_CHAT: GameRules.Key<GameRules.BooleanValue>? = null
    var PROXIMITY_RANGE: GameRules.Key<GameRules.IntegerValue>? = null

    fun register() {
        SHOW_SPELLS_IN_CHAT = GameRules.register(
            "scriptor:show_spells_in_chat",
            GameRules.Category.MISC,
            GameRules.BooleanValue.create(false)
        )
        PROXIMITY_CHAT =
            GameRules.register("scriptor:proximity_chat", GameRules.Category.MISC, GameRules.BooleanValue.create(false))
        PROXIMITY_RANGE = GameRules.register(
            "scriptor:proximity_chat_range",
            GameRules.Category.MISC,
            GameRules.IntegerValue.create(64)
        )
    }
}
