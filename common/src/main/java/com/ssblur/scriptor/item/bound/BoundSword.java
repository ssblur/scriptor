package com.ssblur.scriptor.item.bound;

import com.ssblur.scriptor.color.CustomColors;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import net.fabricmc.api.EnvType;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class BoundSword extends SwordItem implements DyeableLeatherItem {
  public BoundSword(Tier tier, int i, float f, Properties properties) {
    super(tier, i, f, properties);

    if(Platform.getEnv() == EnvType.CLIENT)
      ColorHandlerRegistry.registerItemColors((itemStack, t) -> t == 1 ? getColor(itemStack) : 0xFFFFFF, this);
  }

  @Override
  public int getColor(ItemStack itemStack) {
    long tick = 0;

    if(Platform.getEnv() == EnvType.CLIENT)
      tick = Minecraft.getInstance().level.getGameTime();

    CompoundTag compoundTag = itemStack.getTagElement("display");
    if(compoundTag != null && compoundTag.contains("color", 99))
      return CustomColors.getColor(compoundTag.getInt("color"), tick);
    return 0xa020f0;
  }
}
