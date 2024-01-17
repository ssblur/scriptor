package com.ssblur.scriptor;

import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.*;

public class ScriptorGameRules {
  public static Key<IntegerValue> TOME_MAX_COST;
  public static Key<IntegerValue> VOCAL_MAX_COST;
  public static Key<IntegerValue> VOCAL_HUNGER_THRESHOLD;
  public static Key<IntegerValue> VOCAL_DAMAGE_THRESHOLD;

  public static void register() {
    TOME_MAX_COST = GameRules.register("scriptor:tome_max_cost", Category.MISC, IntegerValue.create(50));
    VOCAL_MAX_COST = GameRules.register("scriptor:vocal_max_cost", Category.MISC, IntegerValue.create(-1));
    VOCAL_HUNGER_THRESHOLD = GameRules.register("scriptor:vocal_hunger_threshold", Category.MISC, IntegerValue.create(50));
    VOCAL_DAMAGE_THRESHOLD = GameRules.register("scriptor:vocal_damage_threshold", Category.MISC, IntegerValue.create(75));
  }
}
