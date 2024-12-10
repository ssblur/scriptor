package com.ssblur.scriptor.trade

import dev.architectury.registry.level.entity.trade.TradeRegistry
import net.minecraft.world.entity.npc.VillagerProfession
import net.minecraft.world.entity.npc.VillagerTrades

object ScriptorTrades {
    var SCROLL_TRADE: VillagerTrades.ItemListing = ScrollTrade()
    var TIER_TRADE: VillagerTrades.ItemListing = TierTrade()

    fun register() {
        TradeRegistry.registerVillagerTrade(VillagerProfession.LIBRARIAN, 3, SCROLL_TRADE)
        TradeRegistry.registerVillagerTrade(VillagerProfession.LIBRARIAN, 3, TIER_TRADE)
    }
}
