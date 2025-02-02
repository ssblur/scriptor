package com.ssblur.scriptor.block

import com.ssblur.scriptor.blockentity.PhasedBlockBlockEntity
import com.ssblur.scriptor.blockentity.ScriptorBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes

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
}
