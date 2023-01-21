package com.ssblur.scriptor;

import com.google.common.base.Suppliers;
import com.ssblur.scriptor.events.SpellChatEvents;
import com.ssblur.scriptor.item.AncientSpellbook;
import com.ssblur.scriptor.item.Spellbook;
import com.ssblur.scriptor.registry.WordRegistry;
import com.ssblur.scriptor.word.action.HealAction;
import com.ssblur.scriptor.word.action.InflameAction;
import com.ssblur.scriptor.word.descriptor.DurationDescriptor;
import com.ssblur.scriptor.word.descriptor.ExtensionDescriptor;
import com.ssblur.scriptor.word.subject.SelfSubject;
import com.ssblur.scriptor.word.subject.TouchSubject;
import dev.architectury.event.events.common.ChatEvent;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
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

  public static void registerWords() {
    WordRegistry.register("self", new SelfSubject());
    WordRegistry.register("touch", new TouchSubject());
    WordRegistry.register("long", new DurationDescriptor());
    WordRegistry.register("strong", new ExtensionDescriptor());
    WordRegistry.register("inflame", new InflameAction());
    WordRegistry.register("heal", new HealAction());
  }

  public static void init() {
    ITEMS.register();
    registerWords();


    ChatEvent.RECEIVED.register(new SpellChatEvents());
  }
}
