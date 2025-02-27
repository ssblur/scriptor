package com.ssblur.scriptor.trade

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.item.ScriptorItems.IDENTIFY_SCROLL
import com.ssblur.scriptor.item.ScriptorItems.TOME_TIER1
import com.ssblur.scriptor.item.ScriptorItems.TOME_TIER2
import com.ssblur.scriptor.item.ScriptorItems.TOME_TIER3
import com.ssblur.scriptor.item.ScriptorItems.TOME_TIER4
import com.ssblur.unfocused.entity.Trades.registerVillagerTrade
import net.minecraft.world.entity.npc.VillagerProfession
import net.minecraft.world.entity.npc.VillagerTrades
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.trading.ItemCost
import net.minecraft.world.item.trading.MerchantOffer

object ScriptorTrades {
  var SCROLL_TRADE: VillagerTrades.ItemListing = VillagerTrades.ItemListing { entity, randomSource ->
    val count = randomSource.nextInt(3) + 1
    MerchantOffer(
      ItemCost(Items.EMERALD, (3 + randomSource.nextInt(2)) * count),
      ItemStack(IDENTIFY_SCROLL.get(), count),
      4,
      5 * count,
      0.1f
    )
  }
  var TIER_TRADE: VillagerTrades.ItemListing = VillagerTrades.ItemListing { entity, randomSource ->
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
    MerchantOffer(ItemCost(book, 1), ItemStack(book2, 2), 1, 15, 0.05f)
  }

  fun register() {
    ScriptorMod.registerVillagerTrade(VillagerProfession.LIBRARIAN, 3, TIER_TRADE)
    ScriptorMod.registerVillagerTrade(VillagerProfession.LIBRARIAN, 3, SCROLL_TRADE)
  }
}
