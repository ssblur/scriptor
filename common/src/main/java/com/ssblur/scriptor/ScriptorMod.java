package com.ssblur.scriptor;

import com.google.common.base.Suppliers;
import com.ssblur.scriptor.commands.DumpDictionaryCommand;
import com.ssblur.scriptor.commands.DumpWordCommand;
import com.ssblur.scriptor.effect.MuteStatusEffect;
import com.ssblur.scriptor.entity.ScriptorProjectile;
import com.ssblur.scriptor.entity.ScriptorProjectileRenderer;
import com.ssblur.scriptor.events.AddLootEvent;
import com.ssblur.scriptor.events.SpellChatEvents;
import com.ssblur.scriptor.events.TomeReloadListener;
import com.ssblur.scriptor.item.AncientSpellbook;
import com.ssblur.scriptor.item.Spellbook;
import com.ssblur.scriptor.messages.TouchNetwork;
import dev.architectury.event.events.common.ChatEvent;
import dev.architectury.event.events.common.CommandRegistrationEvent;
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
  // We can use this if we don't want to use DeferredRegister
  public static final Supplier<Registries> REGISTRIES = Suppliers.memoize(() -> Registries.get(MOD_ID));
  // Registering a new creative tab
  public static final CreativeModeTab SCRIPTOR_TAB = CreativeTabRegistry.create(new ResourceLocation(MOD_ID, "scriptor_tab"), () ->
          new ItemStack(ScriptorMod.SPELLBOOK.get()));

  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registry.ITEM_REGISTRY);
  public static final RegistrySupplier<Item> SPELLBOOK = ITEMS.register("spellbook", () ->
          new Spellbook(new Item.Properties()));
  public static final RegistrySupplier<Item> SPELLBOOK_BINDER = ITEMS.register("spellbook_binder", () ->
    new Item(new Item.Properties().tab(ScriptorMod.SCRIPTOR_TAB)));
  public static final RegistrySupplier<Item> LEATHER_BINDER = ITEMS.register("leather_binder", () ->
    new Item(new Item.Properties().tab(ScriptorMod.SCRIPTOR_TAB)));
  public static final RegistrySupplier<Item> TOME = ITEMS.register("tome", () ->
    new AncientSpellbook(new Item.Properties().tab(ScriptorMod.SCRIPTOR_TAB)));

  public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(MOD_ID, Registry.MOB_EFFECT_REGISTRY);
  public static final RegistrySupplier<MobEffect> HOARSE = EFFECTS.register("hoarse", MuteStatusEffect::new);
  public static final RegistrySupplier<MobEffect> MUTE = EFFECTS.register("mute", MuteStatusEffect::new);

  public static final ResourceLocation GET_TOUCH_DATA = new ResourceLocation(MOD_ID, "get_touch_data");
  public static final ResourceLocation RETURN_TOUCH_DATA = new ResourceLocation(MOD_ID, "return_touch_data");
  public static final NetworkChannel MESSAGES = NetworkChannel.create(new ResourceLocation(MOD_ID, "messages"));

  public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(MOD_ID, Registry.ENTITY_TYPE_REGISTRY);
  public static final RegistrySupplier<EntityType<ScriptorProjectile>> PROJECTILE_TYPE =
    ENTITY_TYPES.register(
      "projectile",
      () -> EntityType.Builder.of(
        ScriptorProjectile::new,
        MobCategory.MISC
      )
        .clientTrackingRange(8)
        .sized(0.25F, 0.25F)
        .build("projectile")
    );

  public static void registerHandlers() {
    ChatEvent.RECEIVED.register(new SpellChatEvents());
    LootEvent.MODIFY_LOOT_TABLE.register(new AddLootEvent());
    ReloadListenerRegistry.register(PackType.SERVER_DATA, TomeReloadListener.INSTANCE);

    if(Platform.getEnv() == EnvType.CLIENT)
      NetworkManager.registerReceiver(NetworkManager.Side.S2C, GET_TOUCH_DATA, TouchNetwork::getTouchData);
    NetworkManager.registerReceiver(NetworkManager.Side.C2S, RETURN_TOUCH_DATA, TouchNetwork::returnTouchData);
  }

  public static void registerRenderers() {
    if(Platform.getEnv() == EnvType.CLIENT)
      EntityRendererRegistry.register(PROJECTILE_TYPE, ScriptorProjectileRenderer::new);
  }

  public static void registerCommands() {
    CommandRegistrationEvent.EVENT.register(DumpDictionaryCommand::register);
    CommandRegistrationEvent.EVENT.register(DumpWordCommand::register);
  }

  public static void init() {
    ITEMS.register();
    EFFECTS.register();
    ENTITY_TYPES.register();

    registerHandlers();
    registerRenderers();
    registerCommands();
  }
}
