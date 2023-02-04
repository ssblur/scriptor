package com.ssblur.scriptor.forge;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.blockentity.ScriptorBlockEntities;
import com.ssblur.scriptor.blockentity.renderers.RuneBlockEntityRenderer;
import dev.architectury.annotations.ForgeEvent;
import dev.architectury.platform.forge.EventBuses;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod(ScriptorMod.MOD_ID)
public class ScriptorModForge {
    public ScriptorModForge() {
      // Submit our event bus to let architectury register our content on the right time
      EventBuses.registerModEventBus(ScriptorMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
      ScriptorMod.init();
      FMLJavaModLoadingContext.get().getModEventBus().addListener(ScriptorModClientEvents::register);
    }
}
