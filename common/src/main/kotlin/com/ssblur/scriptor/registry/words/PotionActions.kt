package com.ssblur.scriptor.registry.words

import com.ssblur.scriptor.api.word.Word
import com.ssblur.scriptor.registry.words.WordRegistry.register
import com.ssblur.scriptor.word.action.potions.*
import net.minecraft.world.effect.MobEffects

@Suppress("unused")
object PotionActions {
    val POISON_POTION = register(
        "poison",
        PotionAction(
            MobEffects.POISON,
            60.0,
            1.0 / 3.0,
            Word.Cost(8.0, Word.COSTTYPE.ADDITIVE)
        )
    )
    val SLOW_POTION = register(
        "slow",
        PotionAction(
            MobEffects.MOVEMENT_SLOWDOWN,
            80.0,
            1.0 / 3.0,
            Word.Cost(6.0, Word.COSTTYPE.ADDITIVE)
        )
    )
    val REGENERATION_POTION = register(
        "regeneration",
        PotionAction(
            MobEffects.REGENERATION,
            30.0,
            1.0 / 3.0,
            Word.Cost(8.0, Word.COSTTYPE.ADDITIVE)
        )
    )
    val WITHER_POTION = register("wither", WitherAction())
    val SATURATION_POTION = register(
        "saturation",
        PotionAction(
            MobEffects.SATURATION,
            4.0,
            1.0 / 3.0,
            Word.Cost(10.0, Word.COSTTYPE.ADDITIVE)
        )
    )
    val SPEED_POTION = register(
        "speed",
        PotionAction(
            MobEffects.MOVEMENT_SPEED,
            80.0,
            1.0 / 2.0,
            Word.Cost(6.0, Word.COSTTYPE.ADDITIVE)
        )
    )
    val HASTE_POTION = register(
        "haste",
        PotionAction(
            MobEffects.DIG_SPEED,
            50.0,
            1.0 / 3.0,
            Word.Cost(8.0, Word.COSTTYPE.ADDITIVE)
        )
    )
    val STRENGTH_POTION = register("strength",
        StrengthAction()
    )
    val JUMP_BOOST_POTION = register(
        "jump_boost",
        PotionAction(
            MobEffects.JUMP,
            60.0,
            1.0 / 3.0,
            Word.Cost(9.0, Word.COSTTYPE.ADDITIVE)
        )
    )
    val RESISTANCE_POTION = register(
        "resistance",
        PotionAction(
            MobEffects.DAMAGE_RESISTANCE,
            50.0,
            1.0 / 3.0,
            Word.Cost(8.0, Word.COSTTYPE.ADDITIVE)
        )
    )
    val FIRE_RESISTANCE_POTION = register(
        "fire_resistance",
        PotionAction(
            MobEffects.FIRE_RESISTANCE,
            30.0,
            1.0 / 3.0,
            Word.Cost(10.0, Word.COSTTYPE.ADDITIVE)
        )
    )
    val WATER_BREATHING_POTION = register(
        "water_breathing",
        PotionAction(
            MobEffects.WATER_BREATHING,
            80.0,
            1.0 / 3.0,
            Word.Cost(6.0, Word.COSTTYPE.ADDITIVE)
        )
    )
    val NIGHT_VISION_POTION = register(
        "night_vision",
        PotionAction(
            MobEffects.NIGHT_VISION,
            80.0,
            1.0 / 3.0,
            Word.Cost(6.0, Word.COSTTYPE.ADDITIVE)
        )
    )
    val WEAKNESS_POTION = register(
        "weakness",
        PotionAction(
            MobEffects.WEAKNESS,
            50.0,
            1.0 / 3.0,
            Word.Cost(6.0, Word.COSTTYPE.ADDITIVE)
        )
    )
    val HERO_POTION = register(
        "hero",
        PotionAction(
            MobEffects.HERO_OF_THE_VILLAGE,
            120.0,
            1.0 / 6.0,
            Word.Cost(100.0, Word.COSTTYPE.ADDITIVE)
        )
    )
    val PHASING_POTION = register("phasing", PhasingAction())
    val WILD_PHASING_POTION = register("wild_phasing",
        WildPhasingAction()
    )
}
