package com.ssblur.scriptor.block

import com.ssblur.scriptor.blockentity.GenerateBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.EnumProperty

class GenerateBlock : Block(Properties.of().air()), EntityBlock {
    enum class Feature(var feature: String) : StringRepresentable {
        NONE("none"),
        ENGRAVING("engraving");

        override fun getSerializedName(): String {
            return this.feature
        }
    }

    init {
        this.registerDefaultState(stateDefinition.any().setValue(FEATURE, Feature.NONE))
    }

    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity =
        GenerateBlockEntity(blockPos, blockState)

    override fun <T : BlockEntity?> getTicker(
        level: Level,
        blockState: BlockState,
        blockEntityType: BlockEntityType<T>
    ) = BlockEntityTicker { tickerLevel, pos, state, entity: T -> GenerateBlockEntity.tick(tickerLevel, pos, state, entity) }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FEATURE)
    }

    companion object {
        val FEATURE: EnumProperty<Feature> = EnumProperty.create("feature", Feature::class.java)
        fun generateEngraving() = ScriptorBlocks.GENERATE.get().defaultBlockState().setValue(FEATURE, Feature.ENGRAVING)
    }
}
