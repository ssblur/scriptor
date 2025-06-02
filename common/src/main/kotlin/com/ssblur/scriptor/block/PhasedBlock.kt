package com.ssblur.scriptor.block

import com.ssblur.scriptor.blockentity.PhasedBlockBlockEntity
import com.ssblur.scriptor.blockentity.ScriptorBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class PhasedBlock: Block(
  Properties.of()
    .noOcclusion()
    .noCollission()
    .strength(-1.0f, 3600000.0f)
    .noLootTable()
    .isValidSpawn { a: BlockState?, b: BlockGetter?, c: BlockPos?, d: EntityType<*>? -> false }
    .forceSolidOn()
    .lightLevel { state: BlockState? -> 3 }
), EntityBlock {
  companion object {
    val WATERLOGGED = BooleanProperty.create("waterlogged")
  }

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
    builder.add(WATERLOGGED)
    super.createBlockStateDefinition(builder)
  }

  override fun getStateForPlacement(blockPlaceContext: BlockPlaceContext): BlockState? {
    return defaultBlockState().setValue(WATERLOGGED, false)
  }

  public override fun getShape(
    blockState: BlockState,
    blockGetter: BlockGetter,
    blockPos: BlockPos,
    collisionContext: CollisionContext
  ) = Shapes.box(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

  override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity =
    ScriptorBlockEntities.PHASED_BLOCK.get().create(blockPos, blockState)!!

  override fun <T: BlockEntity?> getTicker(
    level: Level,
    blockState: BlockState,
    blockEntityType: BlockEntityType<T>
  ) = BlockEntityTicker { tickerLevel: Level?, pos: BlockPos?, state: BlockState?, entity: T ->
    PhasedBlockBlockEntity.tick(entity)
  }

  override fun getBlockSupportShape(blockState: BlockState, blockGetter: BlockGetter, blockPos: BlockPos): VoxelShape =
    Shapes.block()

  override fun getFluidState(blockState: BlockState): FluidState {
    if(blockState.getValue(WATERLOGGED))
      return Fluids.WATER.defaultFluidState()
    return Fluids.EMPTY.defaultFluidState()
  }
}
