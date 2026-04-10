package com.ssblur.scriptor.trade

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.item.ScriptorItems.TOME_TIER1
import com.ssblur.scriptor.item.ScriptorItems.TOME_TIER2
import com.ssblur.scriptor.item.ScriptorItems.TOME_TIER3
import com.ssblur.scriptor.item.ScriptorItems.TOME_TIER4
import com.ssblur.scriptor.villagers.ScriptorVillagers
import com.ssblur.unfocused.entity.Trades.registerVillagerTrade
import net.minecraft.world.entity.npc.Villager
import net.minecraft.world.entity.npc.VillagerTrades
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.trading.ItemCost
import net.minecraft.world.item.trading.MerchantOffer

object ScriptorTrades {
//  var SCROLL_TRADE: VillagerTrades.ItemListing = VillagerTrades.ItemListing { entity, randomSource ->
//    val count = randomSource.nextInt(3) + 1
//    MerchantOffer(
//      ItemCost(Items.EMERALD, (3 + randomSource.nextInt(2)) * count),
//      ItemStack(IDENTIFY_SCROLL.get(), count),
//      4,
//      5 * count,
//      0.1f
//    )
//  }
  var TIER_TRADE: VillagerTrades.ItemListing = VillagerTrades.ItemListing { _, randomSource ->
    val book: Item
    val book2: Item
    when (randomSource.nextInt(3)) {
      0 -> {
        book = TOME_TIER2.get()
        book2 = TOME_TIER1.get()
      }

      1 -> {
        book = TOME_TIER3.get()
        book2 = TOME_TIER2.get()
      }

      else -> {
        book = TOME_TIER4.get()
        book2 = TOME_TIER3.get()
      }
    }
    MerchantOffer(ItemCost(book, 1), ItemStack(book2, 2), 3, 15, 0.05f)
  }
  var TIER1_BOOK_TRADE: VillagerTrades.ItemListing = VillagerTrades.ItemListing { _, _ ->
    MerchantOffer(ItemCost(Items.EMERALD, 3), ItemStack(TOME_TIER1.get(), 1), 2, 15, 0.05f)
  }
  var BUY_MATERIALS_TRADE: VillagerTrades.ItemListing = VillagerTrades.ItemListing { villager, randomSource ->
    val materials = listOf(
      ItemStack(Items.GLOW_INK_SAC, 1),
      ItemStack(Items.INK_SAC, 2),
      ItemStack(Items.PURPLE_WOOL, 3),
      ItemStack(Items.GOLD_NUGGET, 4)
    ).filter {
      it.item !in (villager as Villager).offers.map { o -> o.result.item }
          && it.item !in villager.offers.map { o -> o.itemCostA.item.value() }
    }
    val material = materials[randomSource.nextInt(materials.size)]
    val emeralds = 2 + randomSource.nextInt(2)
    var mats = emeralds / 2 + randomSource.nextInt(1)
    material.count *= mats
    MerchantOffer(ItemCost(material.item, material.count), ItemStack(Items.EMERALD, emeralds), 8, 64, 0.05f)
  }
  var SELL_MATERIALS_TRADE: VillagerTrades.ItemListing = VillagerTrades.ItemListing { villager, randomSource ->
    val materials = listOf(
      ItemStack(Items.GLOW_INK_SAC, 1),
      ItemStack(Items.INK_SAC, 2),
      ItemStack(Items.PURPLE_WOOL, 3),
      ItemStack(Items.GOLD_NUGGET, 4)
    ).filter {
      it.item !in (villager as Villager).offers.map { o -> o.result.item }
          && it.item !in villager.offers.map { o -> o.itemCostA.item.value() }
    }
    val material = materials[randomSource.nextInt(materials.size)]
    val emeralds = 2 + randomSource.nextInt(2)
    var mats = emeralds / 2 + randomSource.nextInt(1)
    material.count *= mats
    MerchantOffer(ItemCost(Items.EMERALD, emeralds), material, 8, 64, 0.05f)
  }

  fun register() {
    ScriptorVillagers.NOMINOMIST.then {
      ScriptorMod.registerVillagerTrade(it, 1, SELL_MATERIALS_TRADE)
      ScriptorMod.registerVillagerTrade(it, 1, BUY_MATERIALS_TRADE)
      ScriptorMod.registerVillagerTrade(it, 2, SELL_MATERIALS_TRADE)
      ScriptorMod.registerVillagerTrade(it, 2, TIER1_BOOK_TRADE)
      ScriptorMod.registerVillagerTrade(it, 3, TIER_TRADE)
      ScriptorMod.registerVillagerTrade(it, 4, TIER_TRADE)
    }
  }
}
