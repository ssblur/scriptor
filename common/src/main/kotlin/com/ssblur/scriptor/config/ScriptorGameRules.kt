package com.ssblur.scriptor.config

import net.minecraft.world.level.GameRules

object ScriptorGameRules {
    @JvmField
    var TOME_MAX_COST: GameRules.Key<GameRules.IntegerValue>? = null
    @JvmField
    var CHALK_MAX_COST: GameRules.Key<GameRules.IntegerValue>? = null
    @JvmField
    var VOCAL_MAX_COST: GameRules.Key<GameRules.IntegerValue>? = null
    @JvmField
    var CASTING_LECTERN_MAX_COST: GameRules.Key<GameRules.IntegerValue>? = null
    @JvmField
    var VOCAL_HUNGER_THRESHOLD: GameRules.Key<GameRules.IntegerValue>? = null
    @JvmField
    var VOCAL_DAMAGE_THRESHOLD: GameRules.Key<GameRules.IntegerValue>? = null
    @JvmField
    var VOCAL_COOLDOWN_MULTIPLIER: GameRules.Key<GameRules.IntegerValue>? = null
    @JvmField
    var TOME_COOLDOWN_MULTIPLIER: GameRules.Key<GameRules.IntegerValue>? = null
    @JvmField
    var CASTING_LECTERN_COOLDOWN_MULTIPLIER: GameRules.Key<GameRules.IntegerValue>? = null

    @JvmField
    var CAN_TARGET_PLAYER_INVENTORIES: GameRules.Key<GameRules.BooleanValue>? = null


    fun register() {
        TOME_MAX_COST =
            GameRules.register("scriptor:tome_max_cost", GameRules.Category.MISC, GameRules.IntegerValue.create(50))
        CHALK_MAX_COST =
            GameRules.register("scriptor:chalk_max_cost", GameRules.Category.MISC, GameRules.IntegerValue.create(250))
        VOCAL_MAX_COST =
            GameRules.register("scriptor:vocal_max_cost", GameRules.Category.MISC, GameRules.IntegerValue.create(-1))
        CASTING_LECTERN_MAX_COST = GameRules.register(
            "scriptor:casting_lectern_max_cost",
            GameRules.Category.MISC,
            GameRules.IntegerValue.create(20)
        )
        VOCAL_HUNGER_THRESHOLD = GameRules.register(
            "scriptor:vocal_hunger_threshold",
            GameRules.Category.MISC,
            GameRules.IntegerValue.create(50)
        )
        VOCAL_DAMAGE_THRESHOLD = GameRules.register(
            "scriptor:vocal_damage_threshold",
            GameRules.Category.MISC,
            GameRules.IntegerValue.create(75)
        )
        VOCAL_COOLDOWN_MULTIPLIER = GameRules.register(
            "scriptor:vocal_cooldown_multiplier",
            GameRules.Category.MISC,
            GameRules.IntegerValue.create(100)
        )
        TOME_COOLDOWN_MULTIPLIER = GameRules.register(
            "scriptor:tome_cooldown_multiplier",
            GameRules.Category.MISC,
            GameRules.IntegerValue.create(100)
        )
        CASTING_LECTERN_COOLDOWN_MULTIPLIER = GameRules.register(
            "scriptor:casting_lectern_cooldown_multiplier",
            GameRules.Category.MISC,
            GameRules.IntegerValue.create(100)
        )

        CAN_TARGET_PLAYER_INVENTORIES = GameRules.register(
            "scriptor:can_target_player_inventories",
            GameRules.Category.MISC,
            GameRules.BooleanValue.create(true)
        )

        ChatRules.register()
    }
}
