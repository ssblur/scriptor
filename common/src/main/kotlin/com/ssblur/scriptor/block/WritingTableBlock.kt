package com.ssblur.scriptor.block

import com.mojang.serialization.MapCodec
import com.ssblur.scriptor.blockentity.ScriptorBlockEntities
import com.ssblur.scriptor.blockentity.WritingTableBlockEntity
import com.ssblur.unfocused.extension.BlockEntityTypeExtension.create
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.Containers
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes

class WritingTableBlock: BaseEntityBlock(Properties.ofFullCopy(Blocks.ACACIA_PLANKS).noOcclusion()) {
  init {
    this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(BOOK, false))
  }

  override fun getStateForPlacement(blockPlaceContext: BlockPlaceContext) =
    defaultBlockState().setValue(FACING, blockPlaceContext.horizontalDirection.opposite)

  override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
    builder.add(FACING).add(BOOK)
  }

  override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity {
    return ScriptorBlockEntities.WRITING_TABLE.create(blockPos, blockState)!!
  }

  override fun useWithoutItem(
    blockState: BlockState,
    level: Level,
    blockPos: BlockPos,
    player: Player,
    blockHitResult: BlockHitResult
  ): InteractionResult {
    if (!level.isClientSide) {
      val provider = blockState.getMenuProvider(level, blockPos)
      if (provider != null) player.openMenu(provider)
    }
    return InteractionResult.SUCCESS
  }

  override fun <T: BlockEntity> getTicker(
    level: Level,
    blockState: BlockState,
    blockEntityType: BlockEntityType<T>
  ): BlockEntityTicker<T> = BlockEntityTicker { tickerLevel, pos: BlockPos?, state: BlockState?, entity ->
    WritingTableBlockEntity.tick(
      tickerLevel,
      entity as BlockEntity
    )
  }

  public override fun getShape(
    blockState: BlockState,
    blockGetter: BlockGetter,
    blockPos: BlockPos,
    collisionContext: CollisionContext
  ) = Shapes.box(2.0/16.0, 0.0, 2.0/16.0, 14.0/16.0, 14.0/16.0, 14.0/16.0)

  override fun codec(): MapCodec<out BaseEntityBlock> = MapCodec.unit(this)

  override fun onRemove(
    blockState: BlockState,
    level: Level,
    blockPos: BlockPos,
    blockState2: BlockState,
    drops: Boolean
  ) {
    if (!level.isClientSide) {
      val table = level.getBlockEntity(blockPos)
      if (table is WritingTableBlockEntity) {
        Containers.dropContentsOnDestroy(blockState, blockState2, level, blockPos)
      }
    }
    super.onRemove(blockState, level, blockPos, blockState2, drops)
  }

  override fun getRenderShape(blockState: BlockState) = RenderShape.MODEL

  companion object {
    val FACING: DirectionProperty = HorizontalDirectionalBlock.FACING
    val BOOK: BooleanProperty = BooleanProperty.create("book")
  }
}
