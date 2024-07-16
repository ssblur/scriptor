package com.ssblur.scriptor.color;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.ssblur.scriptor.color.interfaces.ColorableBlock;
import com.ssblur.scriptor.registry.colorable.ColorableBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Random;

public class DyeColorableBlock implements ColorableBlock {
  BiMap<DyeColor, Block> blocks = HashBiMap.create();
  Random random = new Random();

  @Override
  public void setColor(int color, Level level, BlockPos blockPos) {
    BlockEntity blockEntity = level.getBlockEntity(blockPos);
    level.setBlock(
      blockPos,
      blocks.get(
        CustomColors.getDyeColor(color, level.getGameTime())
      ).withPropertiesOf(level.getBlockState(blockPos)),
      2
    );
    if(blockEntity != null) {
      level.removeBlockEntity(blockPos);
      level.setBlockEntity(blockEntity);
    }
  }

  @Override
  public ItemStack setColor(int color, ItemStack itemStack) {
    var block = blocks.get(CustomColors.getDyeColor(color, Math.abs(random.nextInt())));
    var dyedItemColor = itemStack.get(DataComponents.DYED_COLOR);
    boolean tooltip = true;
    if(dyedItemColor != null)
      tooltip = dyedItemColor.showInTooltip();
    int count = itemStack.getCount();

    var itemOut = new ItemStack(block);
    itemOut.setCount(count);
    itemOut.set(DataComponents.DYED_COLOR, new DyedItemColor(color, tooltip));
    return itemOut;
  }

  public void add(Block block, DyeColor dyeColor) {
    blocks.put(dyeColor, block);
  }

  public void register() {
    for(var entry: blocks.values())
      ColorableBlockRegistry.register(entry, this);
  }
}
