package com.ssblur.scriptor.item.bound;

import com.ssblur.scriptor.color.CustomColors;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import net.fabricmc.api.EnvType;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BoundTool extends DiggerItem implements DyeableLeatherItem {
  TagKey<Block> tagKey;
  public BoundTool(int i, float f, Tier tier, TagKey<Block> tagKey, Properties properties) {
    super(i, f, tier, tagKey, properties);
    this.tagKey = tagKey;

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
      if(scriptor.contains("efficiency") && state.is(tagKey))
        return scriptor.getFloat("efficiency");
    }

    return super.getDestroySpeed(item, state);
  }
}
