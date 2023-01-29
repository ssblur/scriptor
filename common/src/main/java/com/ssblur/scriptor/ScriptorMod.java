package com.ssblur.scriptor;

import com.google.common.base.Suppliers;
import com.ssblur.scriptor.effect.MuteStatusEffect;
import com.ssblur.scriptor.effect.ScriptorEffects;
import com.ssblur.scriptor.entity.ScriptorEntities;
import com.ssblur.scriptor.entity.ScriptorProjectile;
import com.ssblur.scriptor.entity.ScriptorProjectileRenderer;
import com.ssblur.scriptor.events.AddLootEvent;
import com.ssblur.scriptor.events.ScriptorEvents;
import com.ssblur.scriptor.events.SpellChatEvents;
import com.ssblur.scriptor.events.TomeReloadListener;
import com.ssblur.scriptor.item.AncientSpellbook;
import com.ssblur.scriptor.item.ScriptorItems;
import com.ssblur.scriptor.item.Spellbook;
import com.ssblur.scriptor.messages.TouchNetwork;
import dev.architectury.event.events.common.ChatEvent;
import dev.architectury.event.events.common.LootEvent;
import dev.architectury.networking.NetworkChannel;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class ScriptorMod {
  public static final String MOD_ID = "scriptor";
  public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
  public static final Supplier<Registries> REGISTRIES = Suppliers.memoize(() -> Registries.get(MOD_ID));
  public static final CreativeModeTab SCRIPTOR_TAB = CreativeTabRegistry.create(new ResourceLocation(MOD_ID, "scriptor_tab"), () ->
          new ItemStack(ScriptorItems.SPELLBOOK.get()));

  public static void init() {
    ScriptorItems.register();
    ScriptorEntities.register();
    ScriptorEvents.register();
    ScriptorEffects.register();
  }
}
