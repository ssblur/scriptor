package com.ssblur.scriptor.registry.words;

import com.ssblur.scriptor.api.word.Action;
import com.ssblur.scriptor.api.word.Word;
import com.ssblur.scriptor.word.action.PotionAction;
import net.minecraft.world.effect.MobEffects;

public class PotionActions {
  private static final WordRegistry INSTANCE = WordRegistry.INSTANCE;

  public final Action POISON_POTION = INSTANCE.register("poison",
    new PotionAction(MobEffects.POISON, 60, 1d/3d, new Word.Cost(8, Word.COSTTYPE.ADDITIVE)));
  public final Action SLOW_POTION = INSTANCE.register("slow",
    new PotionAction(MobEffects.MOVEMENT_SLOWDOWN, 80, 1d/3d, new Word.Cost(6, Word.COSTTYPE.ADDITIVE)));
  public final Action REGENERATION_POTION = INSTANCE.register("regeneration",
    new PotionAction(MobEffects.REGENERATION, 30, 1d/3d, new Word.Cost(8, Word.COSTTYPE.ADDITIVE)));
  public final Action WITHER_POTION = INSTANCE.register("wither",
    new PotionAction(MobEffects.WITHER, 30, 1d/3d, new Word.Cost(12, Word.COSTTYPE.ADDITIVE)));
  public final Action SATURATION_POTION = INSTANCE.register("saturation",
    new PotionAction(MobEffects.SATURATION, 60, 1d/3d, new Word.Cost(4, Word.COSTTYPE.ADDITIVE)));
  public final Action SPEED_POTION = INSTANCE.register("speed",
    new PotionAction(MobEffects.MOVEMENT_SPEED, 80, 1d/2d, new Word.Cost(6, Word.COSTTYPE.ADDITIVE)));
  public final Action HASTE_POTION = INSTANCE.register("haste",
    new PotionAction(MobEffects.DIG_SPEED, 50, 1d/3d, new Word.Cost(8, Word.COSTTYPE.ADDITIVE)));
  public final Action STRENGTH_POTION = INSTANCE.register("strength",
    new PotionAction(MobEffects.DAMAGE_BOOST, 40, 1d/3d, new Word.Cost(9, Word.COSTTYPE.ADDITIVE)));
  public final Action JUMP_BOOST_POTION = INSTANCE.register("jump_boost",
    new PotionAction(MobEffects.JUMP, 60, 1d/3d, new Word.Cost(9, Word.COSTTYPE.ADDITIVE)));
  public final Action RESISTANCE_POTION = INSTANCE.register("resistance",
    new PotionAction(MobEffects.DAMAGE_RESISTANCE, 50, 1d/3d, new Word.Cost(8, Word.COSTTYPE.ADDITIVE)));
  public final Action FIRE_RESISTANCE_POTION = INSTANCE.register("fire_resistance",
    new PotionAction(MobEffects.FIRE_RESISTANCE, 30, 1d/3d, new Word.Cost(10, Word.COSTTYPE.ADDITIVE)));
  public final Action WATER_BREATHING_POTION = INSTANCE.register("water_breathing",
    new PotionAction(MobEffects.WATER_BREATHING, 80, 1d/3d, new Word.Cost(6, Word.COSTTYPE.ADDITIVE)));
  public final Action NIGHT_VISION_POTION = INSTANCE.register("night_vision",
    new PotionAction(MobEffects.NIGHT_VISION, 80, 1d/3d, new Word.Cost(6, Word.COSTTYPE.ADDITIVE)));
  public final Action WEAKNESS_POTION = INSTANCE.register("weakness",
    new PotionAction(MobEffects.WEAKNESS, 50, 1d/3d, new Word.Cost(6, Word.COSTTYPE.ADDITIVE)));
  public final Action HERO_POTION = INSTANCE.register("hero",
    new PotionAction(MobEffects.HERO_OF_THE_VILLAGE, 120, 1d/6d, new Word.Cost(100, Word.COSTTYPE.ADDITIVE)));
}
