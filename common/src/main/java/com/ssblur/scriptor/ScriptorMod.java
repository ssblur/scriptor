package com.ssblur.scriptor;

import com.google.common.base.Suppliers;
import com.ssblur.scriptor.effect.MuteStatusEffect;
import com.ssblur.scriptor.events.AddLootEvent;
import com.ssblur.scriptor.events.SpellChatEvents;
import com.ssblur.scriptor.item.AncientSpellbook;
import com.ssblur.scriptor.item.Spellbook;
import com.ssblur.scriptor.messages.TouchNetwork;
import com.ssblur.scriptor.registry.WordRegistry;
import com.ssblur.scriptor.word.action.Action;
import com.ssblur.scriptor.word.action.HealAction;
import com.ssblur.scriptor.word.action.InflameAction;
import com.ssblur.scriptor.word.action.SmiteAction;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.DurationDescriptor;
import com.ssblur.scriptor.word.descriptor.ElementalDescriptor;
import com.ssblur.scriptor.word.descriptor.ExtensionDescriptor;
import com.ssblur.scriptor.word.subject.SelfSubject;
import com.ssblur.scriptor.word.subject.StormSubject;
import com.ssblur.scriptor.word.subject.Subject;
import com.ssblur.scriptor.word.subject.TouchSubject;
import dev.architectury.event.events.common.ChatEvent;
import dev.architectury.event.events.common.LootEvent;
import dev.architectury.networking.NetworkChannel;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
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

  public static final WordRegistry WORDS = new WordRegistry();

  public static final Subject SELF = WORDS.register("self", new SelfSubject());
  public static final Subject TOUCH = WORDS.register("touch", new TouchSubject());
  public static final Subject STORM = WORDS.register("storm", new StormSubject());

  public static final Descriptor LONG = WORDS.register("long", new DurationDescriptor());
  public static final Descriptor SLOW = WORDS.register("slow", new DurationDescriptor());
  public static final Descriptor STRONG = WORDS.register("strong", new ExtensionDescriptor());
//  public static final Descriptor WOOD = WORDS.register("wood", new ElementalDescriptor());
//  public static final Descriptor FIRE = WORDS.register("fire", new ElementalDescriptor());
//  public static final Descriptor EARTH = WORDS.register("earth", new ElementalDescriptor());
//  public static final Descriptor GOLD = WORDS.register("gold", new ElementalDescriptor());
//  public static final Descriptor WATER = WORDS.register("water", new ElementalDescriptor());

  public static final Action INFLAME = WORDS.register("inflame", new InflameAction());
  public static final Action HEAL = WORDS.register("heal", new HealAction());
  public static final Action SMITE = WORDS.register("smite", new SmiteAction());

  public static final ResourceLocation GET_TOUCH_DATA = new ResourceLocation(MOD_ID, "get_touch_data");
  public static final ResourceLocation RETURN_TOUCH_DATA = new ResourceLocation(MOD_ID, "return_touch_data");
  public static final NetworkChannel MESSAGES = NetworkChannel.create(new ResourceLocation(MOD_ID, "messages"));

  public static void registerHandlers() {
    ChatEvent.RECEIVED.register(new SpellChatEvents());
    LootEvent.MODIFY_LOOT_TABLE.register(new AddLootEvent());

    if(Platform.getEnv() == EnvType.CLIENT)
      NetworkManager.registerReceiver(NetworkManager.Side.S2C, GET_TOUCH_DATA, TouchNetwork::getTouchData);
    NetworkManager.registerReceiver(NetworkManager.Side.C2S, RETURN_TOUCH_DATA, TouchNetwork::returnTouchData);
  }

  public static void init() {
    ITEMS.register();
    EFFECTS.register();

    registerHandlers();
  }
}
