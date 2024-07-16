package com.ssblur.scriptor.tabs;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.item.ScriptorItems;
import dev.architectury.registry.CreativeTabOutput;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ScriptorTabs {
  public static final String MOD_ID = ScriptorMod.MOD_ID;
  public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(ScriptorMod.MOD_ID, Registries.CREATIVE_MODE_TAB);
  public static final RegistrySupplier<CreativeModeTab> SCRIPTOR_TAB = TABS.register(
    ResourceLocation.tryBuild(MOD_ID, "scriptor_tab"),
    () ->
      CreativeTabRegistry.create(
        Component.translatable("itemGroup.scriptor.scriptor_tab"),
        () ->
          new ItemStack(ScriptorItems.TOME_TIER4.get())
      )
  );

  @SuppressWarnings("UnstableApiUsage")
  public static void register() {
    TABS.register();

    CreativeTabRegistry.modify(SCRIPTOR_TAB, (FeatureFlagSet flags, CreativeTabOutput output, boolean canUseGameMasterBlocks) -> {
      if(!ScriptorMod.COMMUNITY_MODE) {
        output.accept(ScriptorItems.TOME_TIER1.get());
        output.accept(ScriptorItems.TOME_TIER2.get());
        output.accept(ScriptorItems.TOME_TIER3.get());
        output.accept(ScriptorItems.TOME_TIER4.get());
        output.accept(ScriptorItems.SCRAP_TIER1.get());
        output.accept(ScriptorItems.SCRAP_TIER2.get());
        output.accept(ScriptorItems.SCRAP_TIER3.get());
      }
    });
  }
}
