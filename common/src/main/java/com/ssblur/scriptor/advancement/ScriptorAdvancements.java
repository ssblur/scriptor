package com.ssblur.scriptor.advancement;

import com.ssblur.scriptor.ScriptorMod;
import net.minecraft.advancements.CriteriaTriggers;

public class ScriptorAdvancements {
  public static final GenericScriptorTrigger TOME = new GenericScriptorTrigger(ScriptorMod.location("tome_collect"));
  public static final GenericScriptorTrigger TOME_1 = new GenericScriptorTrigger(ScriptorMod.location("tome1_collect"));
  public static final GenericScriptorTrigger TOME_2 = new GenericScriptorTrigger(ScriptorMod.location("tome2_collect"));
  public static final GenericScriptorTrigger TOME_3 = new GenericScriptorTrigger(ScriptorMod.location("tome3_collect"));
  public static final GenericScriptorTrigger TOME_4 = new GenericScriptorTrigger(ScriptorMod.location("tome4_collect"));
  public static final GenericScriptorTrigger COMPLEX_SPELL = new GenericScriptorTrigger(ScriptorMod.location("complex_spell"));
  public static final GenericScriptorTrigger FIZZLE = new GenericScriptorTrigger(ScriptorMod.location("fizzle"));
  public static final GenericScriptorTrigger COMMUNITY = new GenericScriptorTrigger(ScriptorMod.location("community"));

  public static void register() {
    CriteriaTriggers.register(TOME);
    CriteriaTriggers.register(TOME_1);
    CriteriaTriggers.register(TOME_2);
    CriteriaTriggers.register(TOME_3);
    CriteriaTriggers.register(TOME_4);
    CriteriaTriggers.register(COMPLEX_SPELL);
    CriteriaTriggers.register(FIZZLE);
    CriteriaTriggers.register(COMMUNITY);
  }
}
