package com.ssblur.scriptor.block

import com.ssblur.scriptor.blockentity.ScriptorBlockEntities
import com.ssblur.unfocused.extension.BlockEntityTypeExtension.create
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class LightBlock: Block(
  Properties.of()
    .instabreak()
    .noLootTable()
    .sound(SoundType.WOOL)
    .noCollission()
    .lightLevel { _: BlockState? -> 15 }
), EntityBlock {
  public override fun getShape(
    blockState: BlockState,
    blockGetter: BlockGetter,
    blockPos: BlockPos,
    collisionContext: CollisionContext
  ): VoxelShape = Shapes.box(0.25, 0.25, 0.25, 0.75, 0.75, 0.75)

  override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState) =
    ScriptorBlockEntities.LIGHT.create(blockPos, blockState)
}
