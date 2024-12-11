package com.ssblur.scriptor.item

import com.ssblur.scriptor.ScriptorMod.registerItem
import com.ssblur.scriptor.block.ScriptorBlocks
import com.ssblur.scriptor.item.books.*
import com.ssblur.scriptor.item.casters.CoordinateCasterCrystal
import com.ssblur.scriptor.item.casters.PlayerCasterCrystal
import com.ssblur.scriptor.item.tools.Chalk
import com.ssblur.scriptor.item.tools.EngravingTool
import com.ssblur.scriptor.item.tools.bound.BoundAxe
import com.ssblur.scriptor.item.tools.bound.BoundSword
import com.ssblur.scriptor.item.tools.bound.BoundTool
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Tiers

@Suppress("unused", "unstable")
object ScriptorItems {
    val SPELLBOOK = registerItem("spellbook") { Spellbook(Item.Properties()) }
    val OBFUSCATED_SPELLBOOK = registerItem("obfuscated_spellbook") {
        ObfuscatedSpellbook(Item.Properties())
    }
    val SPELLBOOK_WHITE = registerItem("spellbook_white") {
        Spellbook(Item.Properties())
    }
    val OBFUSCATED_SPELLBOOK_WHITE = registerItem("obfuscated_spellbook_white") {
        ObfuscatedSpellbook(Item.Properties())
    }
    val SPELLBOOK_ORANGE = registerItem("spellbook_orange") {
        Spellbook(Item.Properties())
    }
    val OBFUSCATED_SPELLBOOK_ORANGE = registerItem("obfuscated_spellbook_orange") {
        ObfuscatedSpellbook(
            Item.Properties()
        )
    }
    val SPELLBOOK_MAGENTA = registerItem("spellbook_magenta") {
        Spellbook(Item.Properties())
    }
    val OBFUSCATED_SPELLBOOK_MAGENTA = registerItem("obfuscated_spellbook_magenta") {
        ObfuscatedSpellbook(Item.Properties())
    }
    val SPELLBOOK_LIGHT_BLUE = registerItem("spellbook_light_blue") {
        Spellbook(Item.Properties())
    }
    val OBFUSCATED_SPELLBOOK_LIGHT_BLUE = registerItem("obfuscated_spellbook_light_blue") {
        ObfuscatedSpellbook(Item.Properties())
    }
    val SPELLBOOK_YELLOW = registerItem("spellbook_yellow") {
        Spellbook(Item.Properties())
    }
    val OBFUSCATED_SPELLBOOK_YELLOW = registerItem("obfuscated_spellbook_yellow") {
        ObfuscatedSpellbook(Item.Properties())
    }
    val SPELLBOOK_LIME = registerItem("spellbook_lime") {
        Spellbook(Item.Properties())
    }
    val OBFUSCATED_SPELLBOOK_LIME = registerItem("obfuscated_spellbook_lime") {
        ObfuscatedSpellbook(Item.Properties())
    }
    val SPELLBOOK_PINK = registerItem("spellbook_pink") {
        Spellbook(Item.Properties())
    }
    val OBFUSCATED_SPELLBOOK_PINK = registerItem("obfuscated_spellbook_pink") {
        ObfuscatedSpellbook(Item.Properties())
    }
    val SPELLBOOK_GRAY = registerItem("spellbook_gray") {
        Spellbook(Item.Properties())
    }
    val OBFUSCATED_SPELLBOOK_GRAY = registerItem("obfuscated_spellbook_gray") {
        ObfuscatedSpellbook(Item.Properties())
    }
    val SPELLBOOK_LIGHT_GRAY = registerItem("spellbook_light_gray") {
        Spellbook(Item.Properties())
    }
    val OBFUSCATED_SPELLBOOK_LIGHT_GRAY = registerItem("obfuscated_spellbook_light_gray") {
        ObfuscatedSpellbook(Item.Properties())
    }
    val SPELLBOOK_CYAN = registerItem("spellbook_cyan") {
        Spellbook(Item.Properties())
    }
    val OBFUSCATED_SPELLBOOK_CYAN = registerItem("obfuscated_spellbook_cyan") {
        ObfuscatedSpellbook(Item.Properties())
    }
    val SPELLBOOK_BLUE = registerItem("spellbook_blue") {
        Spellbook(Item.Properties())
    }
    val OBFUSCATED_SPELLBOOK_BLUE = registerItem("obfuscated_spellbook_blue") {
        ObfuscatedSpellbook(Item.Properties())
    }
    val SPELLBOOK_BROWN = registerItem("spellbook_brown") {
        Spellbook(Item.Properties())
    }
    val OBFUSCATED_SPELLBOOK_BROWN = registerItem("obfuscated_spellbook_brown") {
        ObfuscatedSpellbook(Item.Properties())
    }
    val SPELLBOOK_GREEN = registerItem("spellbook_green") {
        Spellbook(Item.Properties())
    }
    val OBFUSCATED_SPELLBOOK_GREEN = registerItem("obfuscated_spellbook_green") {
        ObfuscatedSpellbook(Item.Properties())
    }
    val SPELLBOOK_RED = registerItem("spellbook_red") { Spellbook(Item.Properties()) }
    val OBFUSCATED_SPELLBOOK_RED = registerItem("obfuscated_spellbook_red") {
        ObfuscatedSpellbook(Item.Properties())
    }
    val SPELLBOOK_BLACK = registerItem("spellbook_black") {
        Spellbook(Item.Properties())
    }
    val OBFUSCATED_SPELLBOOK_BLACK = registerItem("obfuscated_spellbook_black") {
        ObfuscatedSpellbook(Item.Properties())
    }

    val ARTIFACT_1 = registerItem("artifact_1") { Artifact(Item.Properties()) }
    val ARTIFACT_2 = registerItem("artifact_2") { Artifact(Item.Properties()) }
    val ARTIFACT_3 = registerItem("artifact_3") { Artifact(Item.Properties()) }
    val ARTIFACT_4 = registerItem("artifact_4") { Artifact(Item.Properties()) }

    val BOOK_OF_BOOKS = registerItem("book_of_books") {
        BookOfBooks(Item.Properties().stacksTo(1).`arch$tab`(ScriptorTabs.SCRIPTOR_TAB), 8)
    }
    val SPELLBOOK_BINDER = registerItem("spellbook_binder") {
        Item(Item.Properties().`arch$tab`(ScriptorTabs.SCRIPTOR_TAB))
    }
    val LEATHER_BINDER = registerItem("leather_binder") {
        Item(Item.Properties().`arch$tab`(ScriptorTabs.SCRIPTOR_TAB))
    }
    val WRITABLE_SPELLBOOK = registerItem("writable_spellbook") {
        WritableSpellbook(Item.Properties().`arch$tab`(ScriptorTabs.SCRIPTOR_TAB))
    }
    val SCRAP = registerItem("scrap") { Scrap(Item.Properties()) }
    val CHALK = registerItem("chalk") {
        Chalk(Item.Properties().`arch$tab`(ScriptorTabs.SCRIPTOR_TAB))
    }
    val ENGRAVING_TOOL = registerItem("engraving_tool") {
        EngravingTool(Item.Properties().`arch$tab`(ScriptorTabs.SCRIPTOR_TAB))
    }

    val TOME_TIER1 = registerItem("tome_tier1") {
        AncientSpellbook(Item.Properties(), 1)
    }
    val TOME_TIER2 = registerItem("tome_tier2") {
        AncientSpellbook(Item.Properties(), 2)
    }
    val TOME_TIER3 = registerItem("tome_tier3") {
        AncientSpellbook(Item.Properties(), 3)
    }
    val TOME_TIER4 = registerItem("tome_tier4") {
        AncientSpellbook(Item.Properties(), 4)
    }


    val SCRAP_TIER1 = registerItem("scrap_tier1") {
        AncientScrap(Item.Properties(), 1)
    }
    val SCRAP_TIER2 = registerItem("scrap_tier2") {
        AncientScrap(Item.Properties(), 2)
    }
    val SCRAP_TIER3 = registerItem("scrap_tier3") {
        AncientScrap(Item.Properties(), 3)
    }

    val IDENTIFY_SCROLL = registerItem("identify_scroll") {
        IdentifyScroll(Item.Properties().`arch$tab`(ScriptorTabs.SCRIPTOR_TAB))
    }

    val EMPTY_CASTING_CRYSTAL = registerItem("empty_casting_crystal") {
        Ingredient(Item.Properties().`arch$tab`(ScriptorTabs.SCRIPTOR_TAB), "lore.scriptor.ingredient", "lore.scriptor.needs_charge")
    }
    val CASTING_CRYSTAL = registerItem("casting_crystal") {
        Ingredient(Item.Properties().`arch$tab`(ScriptorTabs.SCRIPTOR_TAB), "lore.scriptor.ingredient")
    }
    val COORDINATE_TAG = registerItem("coordinate_tag") {
        Ingredient(Item.Properties().`arch$tab`(ScriptorTabs.SCRIPTOR_TAB), "lore.scriptor.ingredient", "lore.scriptor.ingredient_tag")
    }
    val PLAYER_TAG = registerItem("player_tag") {
        Ingredient(Item.Properties().`arch$tab`(ScriptorTabs.SCRIPTOR_TAB), "lore.scriptor.ingredient", "lore.scriptor.ingredient_tag")
    }


    val PLAYER_CASTING_CRYSTAL = registerItem("player_casting_crystal") {
        PlayerCasterCrystal(Item.Properties().`arch$tab`(ScriptorTabs.SCRIPTOR_TAB))
    }
    val COORDINATE_CASTING_CRYSTAL = registerItem("coordinate_casting_crystal") {
        CoordinateCasterCrystal(Item.Properties().`arch$tab`(ScriptorTabs.SCRIPTOR_TAB))
    }

    val BOUND_SWORD = registerItem("bound_sword") {
        BoundSword(Tiers.STONE, Item.Properties().durability(Int.MAX_VALUE).`arch$tab`(ScriptorTabs.SCRIPTOR_TAB))
    }
    val BOUND_AXE = registerItem("bound_axe") {
        BoundAxe(Tiers.STONE, Item.Properties().durability(Int.MAX_VALUE).`arch$tab`(ScriptorTabs.SCRIPTOR_TAB))
    }
    val BOUND_SHOVEL = registerItem("bound_shovel") {
        BoundTool(Tiers.STONE, BlockTags.MINEABLE_WITH_SHOVEL, Item.Properties().durability(Int.MAX_VALUE).`arch$tab`(ScriptorTabs.SCRIPTOR_TAB))
    }
    val BOUND_PICKAXE = registerItem("bound_pickaxe") {
        BoundTool(Tiers.STONE, BlockTags.MINEABLE_WITH_PICKAXE, Item.Properties().durability(Int.MAX_VALUE).`arch$tab`(ScriptorTabs.SCRIPTOR_TAB))
    }

    val CASTING_LECTERN = registerItem("casting_lectern") {
        BlockItem(ScriptorBlocks.CASTING_LECTERN.get(), Item.Properties().`arch$tab`(ScriptorTabs.SCRIPTOR_TAB))
    }

    fun register() {}
}