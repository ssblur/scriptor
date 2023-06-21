package com.ssblur.scriptor.registry.colorable;

import com.ssblur.scriptor.color.interfaces.ColorableBlock;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;

public class ColorableBlockRegistry {
  public static ColorableBlockRegistry INSTANCE = new ColorableBlockRegistry();

  HashMap<Block, ColorableBlock> colorableBlocks = new HashMap<>();

  public static DyeColorableBlocks DYE_COLORABLE_BLOCKS = new DyeColorableBlocks();

  public static void register(Block block, ColorableBlock colorableBlock) {
    INSTANCE.colorableBlocks.put(block, colorableBlock);
  }

  public static ColorableBlock get(Block block) {
    return INSTANCE.colorableBlocks.get(block);
  }

  public static boolean has(Block block) {
    return INSTANCE.colorableBlocks.containsKey(block);
  }
}
