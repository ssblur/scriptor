package com.ssblur.scriptor.item.bound;

import com.ssblur.scriptor.color.CustomColors;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import net.fabricmc.api.EnvType;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class BoundSword extends SwordItem {
  public BoundSword(Tier tier, Properties properties) {
    super(tier,properties);

    if(Platform.getEnv() == EnvType.CLIENT)
      ColorHandlerRegistry.registerItemColors((itemStack, t) -> t == 1 ? CustomColors.getColor(itemStack) : 0xFFFFFFFF, this);
  }
}
