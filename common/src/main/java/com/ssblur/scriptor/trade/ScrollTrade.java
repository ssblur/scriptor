package com.ssblur.scriptor.trade;

import com.ssblur.scriptor.item.ScriptorItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import org.jetbrains.annotations.Nullable;

public class ScrollTrade implements VillagerTrades.ItemListing {
  @Nullable
  @Override
  public MerchantOffer getOffer(Entity entity, RandomSource randomSource) {
    int count = randomSource.nextInt(4);
    return new MerchantOffer(
      new ItemStack(Items.EMERALD, (3 + randomSource.nextInt(2)) * count),
      new ItemStack(ScriptorItems.IDENTIFY_SCROLL.get(), count),
      4,
      5 * count,
      0.1f
    );
  }
}
