package com.ssblur.scriptor.loot;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.helpers.loot.ArtifactItemFunction;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class ScriptorLoot {
  public static final DeferredRegister<LootItemFunctionType> LOOT_ITEM_FUNCTION_TYPES = DeferredRegister.create(ScriptorMod.MOD_ID, Registries.LOOT_FUNCTION_TYPE);

  public static final RegistrySupplier<LootItemFunctionType> ARTIFACT = LOOT_ITEM_FUNCTION_TYPES.register("artifact",
    () -> new LootItemFunctionType(new ArtifactItemFunction.ArtifactSerializer())
  );

  public static void register() {
    LOOT_ITEM_FUNCTION_TYPES.register();
  }
}
