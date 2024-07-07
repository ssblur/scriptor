package com.ssblur.scriptor.item.bound;

import com.ssblur.scriptor.color.CustomColors;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import net.fabricmc.api.EnvType;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.BlockState;

public class BoundAxe extends AxeItem implements DyeableLeatherItem {
  public BoundAxe(Tier tier, int i, float f, Properties properties) {
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

  @Override
  public float getDestroySpeed(ItemStack item, BlockState state) {
    var scriptor = item.getTagElement("scriptor");
    if(scriptor != null) {
      if(scriptor.contains("efficiency") && state.is(BlockTags.MINEABLE_WITH_AXE))
        return scriptor.getFloat("efficiency");
    }

    return super.getDestroySpeed(item, state);
  }
}
