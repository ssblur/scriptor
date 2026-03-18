package com.ssblur.scriptor.block

import com.mojang.serialization.MapCodec
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DirectionalBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition

class HighlightBlock(properties: Properties) : DirectionalBlock(properties) {
    override fun codec(): MapCodec<out DirectionalBlock> = MapCodec.unit(this)
    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING)
    }
}