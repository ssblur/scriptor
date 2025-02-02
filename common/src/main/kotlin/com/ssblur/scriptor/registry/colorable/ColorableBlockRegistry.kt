package com.ssblur.scriptor.registry.colorable

import com.ssblur.scriptor.color.interfaces.ColorableBlock
import net.minecraft.world.level.block.Block

object ColorableBlockRegistry {
  var colorableBlocks: HashMap<Block, ColorableBlock> = HashMap()

  var DYE_COLORABLE_BLOCKS = DyeColorableBlocks

  fun register(block: Block, colorableBlock: ColorableBlock) {
    colorableBlocks[block] = colorableBlock
  }

  fun get(block: Block): ColorableBlock? {
    return colorableBlocks[block]
  }

  fun has(block: Block): Boolean {
    return colorableBlocks.containsKey(block)
  }
}
