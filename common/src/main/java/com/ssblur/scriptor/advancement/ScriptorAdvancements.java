package com.ssblur.scriptor.advancement;

import com.ssblur.scriptor.ScriptorMod;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;

public class ScriptorAdvancements {
  public static final GenericScriptorTrigger TOME = new GenericScriptorTrigger(new ResourceLocation(ScriptorMod.MOD_ID, "tome_collect"));
  public static final GenericScriptorTrigger TOME_1 = new GenericScriptorTrigger(new ResourceLocation(ScriptorMod.MOD_ID, "tome1_collect"));
  public static final GenericScriptorTrigger TOME_2 = new GenericScriptorTrigger(new ResourceLocation(ScriptorMod.MOD_ID, "tome2_collect"));
  public static final GenericScriptorTrigger TOME_3 = new GenericScriptorTrigger(new ResourceLocation(ScriptorMod.MOD_ID, "tome3_collect"));
  public static final GenericScriptorTrigger TOME_4 = new GenericScriptorTrigger(new ResourceLocation(ScriptorMod.MOD_ID, "tome4_collect"));
  public static final GenericScriptorTrigger COMPLEX_SPELL = new GenericScriptorTrigger(new ResourceLocation(ScriptorMod.MOD_ID, "complex_spell"));
  public static final GenericScriptorTrigger FIZZLE = new GenericScriptorTrigger(new ResourceLocation(ScriptorMod.MOD_ID, "fizzle"));

  public static void register() {
    CriteriaTriggers.register(TOME.location.toString(), TOME);
    CriteriaTriggers.register(TOME_1.location.toString(), TOME_1);
    CriteriaTriggers.register(TOME_2.location.toString(), TOME_2);
    CriteriaTriggers.register(TOME_3.location.toString(), TOME_3);
    CriteriaTriggers.register(TOME_4.location.toString(), TOME_4);
    CriteriaTriggers.register(COMPLEX_SPELL.location.toString(), COMPLEX_SPELL);
    CriteriaTriggers.register(FIZZLE.location.toString(), FIZZLE);
  }
}
