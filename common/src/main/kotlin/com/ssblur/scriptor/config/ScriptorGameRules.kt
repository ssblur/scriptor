package com.ssblur.scriptor.config

import net.minecraft.world.level.GameRules
import net.minecraft.world.level.GameRules.Category
import net.minecraft.world.level.GameRules.register

object ScriptorGameRules {
    var TOME_MAX_COST: GameRules.Key<GameRules.IntegerValue>? = null
    var CHALK_MAX_COST: GameRules.Key<GameRules.IntegerValue>? = null
    var VOCAL_MAX_COST: GameRules.Key<GameRules.IntegerValue>? = null
    var CASTING_LECTERN_MAX_COST: GameRules.Key<GameRules.IntegerValue>? = null
    var VOCAL_HUNGER_THRESHOLD: GameRules.Key<GameRules.IntegerValue>? = null
    var VOCAL_DAMAGE_THRESHOLD: GameRules.Key<GameRules.IntegerValue>? = null
    var VOCAL_COOLDOWN_MULTIPLIER: GameRules.Key<GameRules.IntegerValue>? = null
    var TOME_COOLDOWN_MULTIPLIER: GameRules.Key<GameRules.IntegerValue>? = null
    var CASTING_LECTERN_COOLDOWN_MULTIPLIER: GameRules.Key<GameRules.IntegerValue>? = null
    var CAN_TARGET_PLAYER_INVENTORIES: GameRules.Key<GameRules.BooleanValue>? = null


    fun register() {
        TOME_MAX_COST = register("scriptor:tome_max_cost", Category.MISC, GameRules.IntegerValue.create(50))
        CHALK_MAX_COST = register("scriptor:chalk_max_cost", Category.MISC, GameRules.IntegerValue.create(250))
        VOCAL_MAX_COST = register("scriptor:vocal_max_cost", Category.MISC, GameRules.IntegerValue.create(-1))
        CASTING_LECTERN_MAX_COST = register("scriptor:casting_lectern_max_cost", Category.MISC, GameRules.IntegerValue.create(20))
        VOCAL_HUNGER_THRESHOLD = register("scriptor:vocal_hunger_threshold", Category.MISC, GameRules.IntegerValue.create(50))
        VOCAL_DAMAGE_THRESHOLD = register("scriptor:vocal_damage_threshold", Category.MISC, GameRules.IntegerValue.create(75))
        VOCAL_COOLDOWN_MULTIPLIER = register("scriptor:vocal_cooldown_multiplier", Category.MISC, GameRules.IntegerValue.create(100))
        TOME_COOLDOWN_MULTIPLIER = register("scriptor:tome_cooldown_multiplier", Category.MISC, GameRules.IntegerValue.create(100))
        CASTING_LECTERN_COOLDOWN_MULTIPLIER = register("scriptor:casting_lectern_cooldown_multiplier", Category.MISC, GameRules.IntegerValue.create(100))
        CAN_TARGET_PLAYER_INVENTORIES = register("scriptor:can_target_player_inventories", Category.MISC, GameRules.BooleanValue.create(true))

        ChatRules.register()
    }
}
