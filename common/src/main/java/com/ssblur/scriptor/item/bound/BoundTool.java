package com.ssblur.scriptor.item.bound;

import com.ssblur.scriptor.color.CustomColors;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import net.fabricmc.api.EnvType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;

public class BoundTool extends DiggerItem {
  TagKey<Block> tagKey;
  public BoundTool(Tier tier, TagKey<Block> tagKey, Properties properties) {
    super(tier, tagKey, properties);
    this.tagKey = tagKey;

    if(Platform.getEnv() == EnvType.CLIENT)
      ColorHandlerRegistry.registerItemColors((itemStack, t) -> t == 1 ? CustomColors.INSTANCE.getColor(itemStack) : 0xFFFFFFFF, this);
  }
}
