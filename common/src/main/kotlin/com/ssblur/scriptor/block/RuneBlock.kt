package com.ssblur.scriptor.block

import com.ssblur.scriptor.blockentity.RuneBlockEntity
import com.ssblur.scriptor.blockentity.ScriptorBlockEntities
import com.ssblur.unfocused.extension.BlockEntityTypeExtension.create
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes

class RuneBlock: Block(
  Properties.of()
    .instabreak()
    .noLootTable()
    .sound(SoundType.AMETHYST)
    .noCollission()
), EntityBlock {
  public override fun getShape(
    blockState: BlockState,
    blockGetter: BlockGetter,
    blockPos: BlockPos,
    collisionContext: CollisionContext
  ) = Shapes.box(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0)

  override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState) =
    ScriptorBlockEntities.RUNE.create(blockPos, blockState)

  override fun <T: BlockEntity?> getTicker(
    level: Level,
    blockState: BlockState,
    blockEntityType: BlockEntityType<T>
  ) = BlockEntityTicker { tickerLevel, pos, state, entity: T -> RuneBlockEntity.tick(tickerLevel, entity) }
}
