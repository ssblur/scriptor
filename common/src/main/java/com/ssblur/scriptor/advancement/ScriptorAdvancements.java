package com.ssblur.scriptor.advancement;

import com.ssblur.scriptor.ScriptorMod;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ScriptorAdvancements {
  public static final TomeCollectionTrigger TOME = new TomeCollectionTrigger(new ResourceLocation(ScriptorMod.MOD_ID, "tome_collect"));
  public static final TomeCollectionTrigger TOME_1 = new TomeCollectionTrigger(new ResourceLocation(ScriptorMod.MOD_ID, "tome1_collect"));
  public static final TomeCollectionTrigger TOME_2 = new TomeCollectionTrigger(new ResourceLocation(ScriptorMod.MOD_ID, "tome2_collect"));
  public static final TomeCollectionTrigger TOME_3 = new TomeCollectionTrigger(new ResourceLocation(ScriptorMod.MOD_ID, "tome3_collect"));
  public static final TomeCollectionTrigger TOME_4 = new TomeCollectionTrigger(new ResourceLocation(ScriptorMod.MOD_ID, "tome4_collect"));

  public static void register() {
    CriteriaTriggers.register(TOME);
    CriteriaTriggers.register(TOME_1);
    CriteriaTriggers.register(TOME_2);
    CriteriaTriggers.register(TOME_3);
    CriteriaTriggers.register(TOME_4);
  }
}
