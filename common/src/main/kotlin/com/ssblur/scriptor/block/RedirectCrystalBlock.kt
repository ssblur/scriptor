package com.ssblur.scriptor.block

import com.mojang.serialization.MapCodec
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.DirectionalBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition

class RedirectCrystalBlock: DirectionalBlock(Properties.ofFullCopy(Blocks.AMETHYST_BLOCK).noOcclusion()) {
  override fun codec(): MapCodec<out DirectionalBlock?> = MapCodec.unit(this)
  override fun getStateForPlacement(blockPlaceContext: BlockPlaceContext): BlockState =
    defaultBlockState().setValue(FACING, blockPlaceContext.clickedFace)

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
    builder.add(FACING)
  }
}