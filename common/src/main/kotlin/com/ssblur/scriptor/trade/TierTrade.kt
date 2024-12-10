package com.ssblur.scriptor.trade

import com.ssblur.scriptor.item.ScriptorItems.TOME_TIER1
import com.ssblur.scriptor.item.ScriptorItems.TOME_TIER2
import com.ssblur.scriptor.item.ScriptorItems.TOME_TIER3
import com.ssblur.scriptor.item.ScriptorItems.TOME_TIER4
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.npc.VillagerTrades
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.trading.ItemCost
import net.minecraft.world.item.trading.MerchantOffer

class TierTrade : VillagerTrades.ItemListing {
    override fun getOffer(entity: Entity, randomSource: RandomSource): MerchantOffer? {
        val tier = randomSource.nextInt(3)

        val book: Item
        val book2: Item
        when (tier) {
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
        return MerchantOffer(
            ItemCost(book, 1),
            ItemStack(book2, 2),
            1,
            15,
            0.05f
        )
    }
}
