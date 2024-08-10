package com.ssblur.scriptor.tabs;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.item.ScriptorItems;
import dev.architectury.platform.Platform;
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
  public static final RegistrySupplier<CreativeModeTab> SCRIPTOR_SPELLBOOKS_TAB = TABS.register(
    ResourceLocation.tryBuild(MOD_ID, "scriptor_spellbooks"),
    () ->
      CreativeTabRegistry.create(
        Component.translatable("itemGroup.scriptor.scriptor_spellbooks"),
        () ->
          new ItemStack(ScriptorItems.SPELLBOOK.get())
      )
  );

  @SuppressWarnings("UnstableApiUsage")
  public static void register() {
    TABS.register();

    if(!Platform.isNeoForge())
      CreativeTabRegistry.modify(SCRIPTOR_TAB, (FeatureFlagSet flags, CreativeTabOutput output, boolean canUseGameMasterBlocks) -> {
        if(!ScriptorMod.COMMUNITY_MODE) {
          ScriptorItems.TOME_TIER1.listen(output::accept);
          ScriptorItems.TOME_TIER2.listen(output::accept);
          ScriptorItems.TOME_TIER3.listen(output::accept);
          ScriptorItems.TOME_TIER4.listen(output::accept);
          ScriptorItems.SCRAP_TIER1.listen(output::accept);
          ScriptorItems.SCRAP_TIER2.listen(output::accept);
          ScriptorItems.SCRAP_TIER3.listen(output::accept);
        }
      });
  }
}
