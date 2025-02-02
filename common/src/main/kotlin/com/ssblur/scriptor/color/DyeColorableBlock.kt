package com.ssblur.scriptor.color

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.ssblur.scriptor.color.interfaces.ColorableBlock
import com.ssblur.scriptor.registry.colorable.ColorableBlockRegistry.register
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.DyedItemColor
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import java.util.*
import kotlin.math.abs

class DyeColorableBlock: ColorableBlock {
  var blocks: BiMap<DyeColor, Block> = HashBiMap.create()
  var random: Random = Random()

  override fun setColor(color: Int, level: Level?, blockPos: BlockPos) {
    if (level == null) return
    val blockEntity = level.getBlockEntity(blockPos)
    level.setBlock(
      blockPos,
      blocks[CustomColors.getDyeColor(color, level.gameTime.toFloat())]!!.withPropertiesOf(
        level.getBlockState(
          blockPos
        )
      ),
      2
    )
    if (blockEntity != null) {
      level.removeBlockEntity(blockPos)
      level.setBlockEntity(blockEntity)
    }
  }

  override fun setColor(color: Int, itemStack: ItemStack?): ItemStack {
    val block = blocks[CustomColors.getDyeColor(
      color, abs(random.nextInt().toDouble())
        .toFloat()
    )]!!
    val dyedItemColor = itemStack?.get(DataComponents.DYED_COLOR)
    var tooltip = true
    if (dyedItemColor != null) tooltip = dyedItemColor.showInTooltip()
    val count = itemStack?.count ?: 1

    val itemOut = ItemStack(block)
    itemOut.count = count
    itemOut.set(DataComponents.DYED_COLOR, DyedItemColor(color, tooltip))
    return itemOut
  }

  fun add(block: Block, dyeColor: DyeColor) {
    blocks[dyeColor] = block
  }

  fun register() {
    for (entry in blocks.values) register(entry!!, this)
  }
}
