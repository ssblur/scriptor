package com.ssblur.scriptor.block

import com.mojang.serialization.MapCodec
import com.ssblur.unfocused.extension.BlockExtension.renderType
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.renderer.RenderType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DirectionalBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition

class HighlightBlock(properties: Properties) : DirectionalBlock(properties) {
    override fun codec(): MapCodec<out DirectionalBlock> = MapCodec.unit(this)
    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    init {
        try {
            clientInit()
        } catch (_: NoSuchMethodError) {
        }
    }

    @Environment(EnvType.CLIENT)
    fun clientInit() {
        this.renderType(RenderType.translucent())
    }
}