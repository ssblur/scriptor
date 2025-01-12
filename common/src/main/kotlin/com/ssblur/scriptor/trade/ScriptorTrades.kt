package com.ssblur.scriptor.trade

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.unfocused.entity.Trades.registerVillagerTrade
import net.minecraft.world.entity.npc.VillagerProfession
import net.minecraft.world.entity.npc.VillagerTrades

object ScriptorTrades {
    var SCROLL_TRADE: VillagerTrades.ItemListing = ScrollTrade()
    var TIER_TRADE: VillagerTrades.ItemListing = TierTrade()

    fun register() {
        ScriptorMod.registerVillagerTrade(VillagerProfession.LIBRARIAN, 3, TIER_TRADE)
        ScriptorMod.registerVillagerTrade(VillagerProfession.LIBRARIAN, 3, SCROLL_TRADE)
    }
}
