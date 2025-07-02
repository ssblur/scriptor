package com.ssblur.scriptor.block

import com.ssblur.scriptor.blockentity.EngravingBlockEntity
import com.ssblur.scriptor.blockentity.ScriptorBlockEntities
import com.ssblur.unfocused.extension.BlockEntityTypeExtension.create
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty

class EngravingBlock: ChalkBlock(
  Properties.of()
    .destroyTime(5f)
    .noLootTable()
    .sound(SoundType.STONE)
    .noCollission()
) {
  init {
    this.registerDefaultState(stateDefinition.any()
      .setValue(HIGHLIGHT, false)
      .setValue(SPENT, false)
    )
  }

  override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity =
    ScriptorBlockEntities.ENGRAVING.create(blockPos, blockState)!!

  override fun <T: BlockEntity?> getTicker(
    level: Level,
    blockState: BlockState,
    blockEntityType: BlockEntityType<T>
  ) = BlockEntityTicker { tickerLevel, pos, state, entity: T ->
    EngravingBlockEntity.tick(tickerLevel, entity as BlockEntity)
  }

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
    builder.add(HIGHLIGHT).add(SPENT)
  }

  companion object {
    val SPENT: BooleanProperty = BooleanProperty.create("spent")
    val HIGHLIGHT: BooleanProperty = BooleanProperty.create("highlight")
  }
}
