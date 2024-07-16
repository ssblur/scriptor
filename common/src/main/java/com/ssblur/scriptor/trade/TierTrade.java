package com.ssblur.scriptor.trade;

import com.ssblur.scriptor.item.ScriptorItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.Nullable;

public class TierTrade implements VillagerTrades.ItemListing {
  @Nullable
  @Override
  public MerchantOffer getOffer(Entity entity, RandomSource randomSource) {
    int tier = randomSource.nextInt(3);

    Item book;
    Item book2;
    switch (tier) {
      case 0 -> {
        book = ScriptorItems.TOME_TIER2.get();
        book2 = ScriptorItems.TOME_TIER1.get();
      }
      case 1 -> {
        book = ScriptorItems.TOME_TIER3.get();
        book2 = ScriptorItems.TOME_TIER2.get();
      }
      default -> {
        book = ScriptorItems.TOME_TIER4.get();
        book2 = ScriptorItems.TOME_TIER3.get();
      }
    }

    return new MerchantOffer(
      new ItemCost(book, 1),
      new ItemStack(book2, 2),
      1,
      15,
      0.05f
    );
  }
}
