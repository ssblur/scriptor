package com.ssblur.scriptor.item

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.ScriptorMod.COMMUNITY_MODE
import dev.architectury.platform.Platform
import dev.architectury.registry.CreativeTabOutput
import dev.architectury.registry.CreativeTabRegistry
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack

object ScriptorTabs {
    const val MOD_ID: String = ScriptorMod.MOD_ID
    val TABS: DeferredRegister<CreativeModeTab> =
        DeferredRegister.create(ScriptorMod.MOD_ID, Registries.CREATIVE_MODE_TAB)
    @JvmField
    val SCRIPTOR_TAB: RegistrySupplier<CreativeModeTab> = TABS.register(
        ResourceLocation.tryBuild(MOD_ID, "scriptor_tab")
    ) {
        CreativeTabRegistry.create(
            Component.translatable("itemGroup.scriptor.scriptor_tab")
        ) { ItemStack(ScriptorItems.TOME_TIER4.get()) }
    }
    @JvmField
    val SCRIPTOR_SPELLBOOKS_TAB: RegistrySupplier<CreativeModeTab> = TABS.register(
        ResourceLocation.tryBuild(MOD_ID, "scriptor_spellbooks")
    ) {
        CreativeTabRegistry.create(
            Component.translatable("itemGroup.scriptor.scriptor_spellbooks")
        ) { ItemStack(ScriptorItems.SPELLBOOK.get()) }
    }

    fun register() {
        TABS.register()

        if (!Platform.isNeoForge()) CreativeTabRegistry.modify(SCRIPTOR_TAB) { flags: FeatureFlagSet?, output: CreativeTabOutput?, canUseGameMasterBlocks: Boolean ->
            if (!COMMUNITY_MODE) {
                output?.accept(ScriptorItems.TOME_TIER1.get())
                output?.accept(ScriptorItems.TOME_TIER2.get())
                output?.accept(ScriptorItems.TOME_TIER3.get())
                output?.accept(ScriptorItems.TOME_TIER4.get())
                output?.accept(ScriptorItems.SCRAP_TIER1.get())
                output?.accept(ScriptorItems.SCRAP_TIER2.get())
                output?.accept(ScriptorItems.SCRAP_TIER3.get())
            }
        }
    }
}
