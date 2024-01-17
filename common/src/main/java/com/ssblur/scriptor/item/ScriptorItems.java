package com.ssblur.scriptor.item;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.block.ScriptorBlocks;
import com.ssblur.scriptor.item.casters.CoordinateCasterCrystal;
import com.ssblur.scriptor.item.casters.PlayerCasterCrystal;
import com.ssblur.scriptor.tabs.ScriptorTabs;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

@SuppressWarnings("UnstableApiUsage")
public class ScriptorItems {
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ScriptorMod.MOD_ID, Registries.ITEM);
  public static final RegistrySupplier<Item> SPELLBOOK = ITEMS.register("spellbook", () ->
    new Spellbook(new Item.Properties()));
  public static final RegistrySupplier<Item> OBFUSCATED_SPELLBOOK = ITEMS.register("obfuscated_spellbook", () ->
    new ObfuscatedSpellbook(new Item.Properties()));
  public static final RegistrySupplier<Item> ARTIFACT_1 = ITEMS.register("artifact_1", () ->
    new Artifact(new Item.Properties()));
  public static final RegistrySupplier<Item> ARTIFACT_2 = ITEMS.register("artifact_2", () ->
    new Artifact(new Item.Properties()));
  public static final RegistrySupplier<Item> ARTIFACT_3 = ITEMS.register("artifact_3", () ->
    new Artifact(new Item.Properties()));
  public static final RegistrySupplier<Item> ARTIFACT_4 = ITEMS.register("artifact_4", () ->
    new Artifact(new Item.Properties()));

  public static final RegistrySupplier<Item> BOOK_OF_BOOKS = ITEMS.register("book_of_books", () ->
    new BookOfBooks(new Item.Properties().stacksTo(1).arch$tab(ScriptorTabs.SCRIPTOR_TAB), 4));
  public static final RegistrySupplier<Item> SPELLBOOK_BINDER = ITEMS.register("spellbook_binder", () ->
    new Item(new Item.Properties().arch$tab(ScriptorTabs.SCRIPTOR_TAB)));
  public static final RegistrySupplier<Item> LEATHER_BINDER = ITEMS.register("leather_binder", () ->
    new Item(new Item.Properties().arch$tab(ScriptorTabs.SCRIPTOR_TAB)));
  public static final RegistrySupplier<Item> SCRAP = ITEMS.register("scrap", () ->
    new Scrap(new Item.Properties()));
  public static final RegistrySupplier<Item> CHALK = ITEMS.register("chalk", () ->
    new Chalk(new Item.Properties().arch$tab(ScriptorTabs.SCRIPTOR_TAB)));

  public static final RegistrySupplier<Item> TOME_TIER1 = ITEMS.register("tome_tier1", () ->
    new AncientSpellbook(new Item.Properties(), 1));
  public static final RegistrySupplier<Item> TOME_TIER2 = ITEMS.register("tome_tier2", () ->
    new AncientSpellbook(new Item.Properties(), 2));
  public static final RegistrySupplier<Item> TOME_TIER3 = ITEMS.register("tome_tier3", () ->
    new AncientSpellbook(new Item.Properties(), 3));
  public static final RegistrySupplier<Item> TOME_TIER4 = ITEMS.register("tome_tier4", () ->
    new AncientSpellbook(new Item.Properties(), 4));


  public static final RegistrySupplier<Item> SCRAP_TIER1 = ITEMS.register("scrap_tier1", () ->
    new AncientScrap(new Item.Properties(), 1));
  public static final RegistrySupplier<Item> SCRAP_TIER2 = ITEMS.register("scrap_tier2", () ->
    new AncientScrap(new Item.Properties(), 2));
  public static final RegistrySupplier<Item> SCRAP_TIER3 = ITEMS.register("scrap_tier3", () ->
    new AncientScrap(new Item.Properties(), 3));

  public static final RegistrySupplier<Item> IDENTIFY_SCROLL = ITEMS.register("identify_scroll", () ->
    new IdentifyScroll(new Item.Properties().arch$tab(ScriptorTabs.SCRIPTOR_TAB)));

  public static final RegistrySupplier<Item> EMPTY_CASTING_CRYSTAL = ITEMS.register("empty_casting_crystal", () ->
    new Ingredient(new Item.Properties().arch$tab(ScriptorTabs.SCRIPTOR_TAB), "lore.scriptor.ingredient", "lore.scriptor.needs_charge"));
  public static final RegistrySupplier<Item> CASTING_CRYSTAL = ITEMS.register("casting_crystal", () ->
    new Ingredient(new Item.Properties().arch$tab(ScriptorTabs.SCRIPTOR_TAB), "lore.scriptor.ingredient"));
  public static final RegistrySupplier<Item> COORDINATE_TAG = ITEMS.register("coordinate_tag", () ->
    new Ingredient(new Item.Properties().arch$tab(ScriptorTabs.SCRIPTOR_TAB), "lore.scriptor.ingredient", "lore.scriptor.ingredient_tag"));
  public static final RegistrySupplier<Item> PLAYER_TAG = ITEMS.register("player_tag", () ->
    new Ingredient(new Item.Properties().arch$tab(ScriptorTabs.SCRIPTOR_TAB), "lore.scriptor.ingredient", "lore.scriptor.ingredient_tag"));


  public static final RegistrySupplier<Item> PLAYER_CASTING_CRYSTAL = ITEMS.register("player_casting_crystal", () ->
    new PlayerCasterCrystal(new Item.Properties().arch$tab(ScriptorTabs.SCRIPTOR_TAB)));
  public static final RegistrySupplier<Item> COORDINATE_CASTING_CRYSTAL = ITEMS.register("coordinate_casting_crystal", () ->
    new CoordinateCasterCrystal(new Item.Properties().arch$tab(ScriptorTabs.SCRIPTOR_TAB)));

  public static final RegistrySupplier<Item> CASTING_LECTERN = ITEMS.register("casting_lectern", () ->
    new BlockItem(ScriptorBlocks.CASTING_LECTERN.get(), new Item.Properties().arch$tab(ScriptorTabs.SCRIPTOR_TAB)));

  public static void register() {
    ITEMS.register();
  }
}
