package com.ssblur.scriptor.gamerules;

import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameRules.BooleanValue;
import net.minecraft.world.level.GameRules.Key;

public class ChatRules {
  public static Key<BooleanValue> PROXIMITY_CHAT;
  public static Key<BooleanValue> SHOW_SPELLS_IN_CHAT;
  public static Key<GameRules.IntegerValue> PROXIMITY_RANGE;

  public static void register() {
    SHOW_SPELLS_IN_CHAT = GameRules.register("scriptor:show_spells_in_chat", GameRules.Category.MISC, GameRules.BooleanValue.create(false));
    PROXIMITY_CHAT = GameRules.register("scriptor:proximity_chat", GameRules.Category.MISC, GameRules.BooleanValue.create(false));
    PROXIMITY_RANGE = GameRules.register("scriptor:proximity_chat_range", GameRules.Category.MISC, GameRules.IntegerValue.create(64));
  }
}
