package com.ssblur.scriptor.item.bound;

import com.ssblur.scriptor.color.CustomColors;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import net.fabricmc.api.EnvType;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Tier;

public class BoundAxe extends AxeItem {
  public BoundAxe(Tier tier, Properties properties) {
    super(tier, properties);

    if(Platform.getEnv() == EnvType.CLIENT)
      ColorHandlerRegistry.registerItemColors((itemStack, t) -> t == 1 ? CustomColors.INSTANCE.getColor(itemStack) : 0xFFFFFFFF, this);
  }

}
