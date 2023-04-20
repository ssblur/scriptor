package com.ssblur.scriptor.trade;

import dev.architectury.registry.level.entity.trade.TradeRegistry;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

public class ScriptorTrades {
  public static VillagerTrades.ItemListing SCROLL_TRADE = new ScrollTrade();
  public static VillagerTrades.ItemListing TIER_TRADE = new TierTrade();

  public static void register() {
    TradeRegistry.registerVillagerTrade(VillagerProfession.LIBRARIAN, 3, SCROLL_TRADE);
    TradeRegistry.registerVillagerTrade(VillagerProfession.LIBRARIAN, 3, TIER_TRADE);
  }
}
