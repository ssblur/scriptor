package com.ssblur.scriptor.item

import com.ssblur.scriptor.ScriptorMod.registerItem
import com.ssblur.scriptor.item.books.*
import com.ssblur.scriptor.item.casters.CoordinateCasterCrystal
import com.ssblur.scriptor.item.casters.PlayerCasterCrystal
import com.ssblur.scriptor.item.tools.Chalk
import com.ssblur.scriptor.item.tools.EngravingTool
import com.ssblur.scriptor.item.tools.bound.BoundAxe
import com.ssblur.scriptor.item.tools.bound.BoundSword
import com.ssblur.scriptor.item.tools.bound.BoundTool
import com.ssblur.unfocused.helper.ColorHelper
import com.ssblur.unfocused.tab.CreativeTabs.tab
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.Tiers

@Suppress("unused", "unstable")
object ScriptorItems {
    val SPELLBOOK = registerItem("spellbook") { Spellbook(Item.Properties()) }
    val OBFUSCATED_SPELLBOOK = registerItem("obfuscated_spellbook") { ObfuscatedSpellbook(Item.Properties()) }
    val SPELLBOOKS = ColorHelper.forEachColor {
        if(it.dyeColor != DyeColor.PURPLE)
            registerItem("spellbook_${it.nameAllLowerCase}") { Spellbook(Item.Properties()) }
    }
    val OBFUCATED_SPELLBOOKS = ColorHelper.forEachColor {
        if(it.dyeColor != DyeColor.PURPLE)
            registerItem("obfuscated_spellbook_${it.nameAllLowerCase}") { Spellbook(Item.Properties()) }
    }

    val ARTIFACT_1 = registerItem("artifact_1") { Artifact(Item.Properties()) }
    val ARTIFACT_2 = registerItem("artifact_2") { Artifact(Item.Properties()) }
    val ARTIFACT_3 = registerItem("artifact_3") { Artifact(Item.Properties()) }
    val ARTIFACT_4 = registerItem("artifact_4") { Artifact(Item.Properties()) }

    val BOOK_OF_BOOKS = registerItem("book_of_books") {
        BookOfBooks(Item.Properties().stacksTo(1), 8)
    }.tab(ScriptorTabs.SCRIPTOR_TAB)
    val SPELLBOOK_BINDER = registerItem("spellbook_binder") {
        Item(Item.Properties())
    }.tab(ScriptorTabs.SCRIPTOR_TAB)
    val LEATHER_BINDER = registerItem("leather_binder") {
        Item(Item.Properties())
    }.tab(ScriptorTabs.SCRIPTOR_TAB)
    val WRITABLE_SPELLBOOK = registerItem("writable_spellbook") {
        WritableSpellbook(Item.Properties())
    }.tab(ScriptorTabs.SCRIPTOR_TAB)
    val SCRAP = registerItem("scrap") { Scrap(Item.Properties()) }
    val CHALK = registerItem("chalk") { Chalk(Item.Properties()) }.tab(ScriptorTabs.SCRIPTOR_TAB)
    val ENGRAVING_TOOL = registerItem("engraving_tool") { EngravingTool(Item.Properties()) }

    val TOME_TIER1 = registerItem("tome_tier1") { AncientSpellbook(Item.Properties(), 1) }
    val TOME_TIER2 = registerItem("tome_tier2") { AncientSpellbook(Item.Properties(), 2) }
    val TOME_TIER3 = registerItem("tome_tier3") { AncientSpellbook(Item.Properties(), 3) }
    val TOME_TIER4 = registerItem("tome_tier4") { AncientSpellbook(Item.Properties(), 4) }

    val SCRAP_TIER1 = registerItem("scrap_tier1") { AncientScrap(Item.Properties(), 1) }
    val SCRAP_TIER2 = registerItem("scrap_tier2") { AncientScrap(Item.Properties(), 2) }
    val SCRAP_TIER3 = registerItem("scrap_tier3") { AncientScrap(Item.Properties(), 3) }

    val IDENTIFY_SCROLL = registerItem("identify_scroll") { IdentifyScroll(Item.Properties()) }.tab(ScriptorTabs.SCRIPTOR_TAB)

    val EMPTY_CASTING_CRYSTAL = registerItem("empty_casting_crystal") {
        Ingredient(Item.Properties(), "lore.scriptor.ingredient", "lore.scriptor.needs_charge")
    }.tab(ScriptorTabs.SCRIPTOR_TAB)
    val CASTING_CRYSTAL = registerItem("casting_crystal") {
        Ingredient(Item.Properties(), "lore.scriptor.ingredient")
    }.tab(ScriptorTabs.SCRIPTOR_TAB)
    val COORDINATE_TAG = registerItem("coordinate_tag") {
        Ingredient(Item.Properties(), "lore.scriptor.ingredient", "lore.scriptor.ingredient_tag")
    }.tab(ScriptorTabs.SCRIPTOR_TAB)
    val PLAYER_TAG = registerItem("player_tag") {
        Ingredient(Item.Properties(), "lore.scriptor.ingredient", "lore.scriptor.ingredient_tag")
    }

    val PLAYER_CASTING_CRYSTAL = registerItem("player_casting_crystal") {
        PlayerCasterCrystal(Item.Properties())
    }.tab(ScriptorTabs.SCRIPTOR_TAB)
    val COORDINATE_CASTING_CRYSTAL = registerItem("coordinate_casting_crystal") {
        CoordinateCasterCrystal(Item.Properties())
    }.tab(ScriptorTabs.SCRIPTOR_TAB)

    val BOUND_SWORD = registerItem("bound_sword") {
        BoundSword(Tiers.STONE, Item.Properties().durability(Int.MAX_VALUE))
    }.tab(ScriptorTabs.SCRIPTOR_TAB)
    val BOUND_AXE = registerItem("bound_axe") {
        BoundAxe(Tiers.STONE, Item.Properties().durability(Int.MAX_VALUE))
    }.tab(ScriptorTabs.SCRIPTOR_TAB)
    val BOUND_SHOVEL = registerItem("bound_shovel") {
        BoundTool(Tiers.STONE, BlockTags.MINEABLE_WITH_SHOVEL, Item.Properties().durability(Int.MAX_VALUE))
    }.tab(ScriptorTabs.SCRIPTOR_TAB)
    val BOUND_PICKAXE = registerItem("bound_pickaxe") {
        BoundTool(Tiers.STONE, BlockTags.MINEABLE_WITH_PICKAXE, Item.Properties().durability(Int.MAX_VALUE))
    }.tab(ScriptorTabs.SCRIPTOR_TAB)

    fun register() {}
}