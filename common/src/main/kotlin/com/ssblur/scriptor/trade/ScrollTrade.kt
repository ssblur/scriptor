package com.ssblur.scriptor.trade

import com.ssblur.scriptor.item.ScriptorItems.IDENTIFY_SCROLL
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.npc.VillagerTrades
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.trading.ItemCost
import net.minecraft.world.item.trading.MerchantOffer

class ScrollTrade : VillagerTrades.ItemListing {
    override fun getOffer(entity: Entity, randomSource: RandomSource): MerchantOffer? {
        val count = randomSource.nextInt(4)
        return MerchantOffer(
            ItemCost(Items.EMERALD, (3 + randomSource.nextInt(2)) * count),
            ItemStack(IDENTIFY_SCROLL.get(), count),
            4,
            5 * count,
            0.1f
        )
    }
}
