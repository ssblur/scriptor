package com.ssblur.scriptor.block

import com.ssblur.scriptor.blockentity.ChalkBlockEntity
import com.ssblur.scriptor.blockentity.ScriptorBlockEntities
import com.ssblur.scriptor.data.components.DictionaryData
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.item.ScriptorItems
import com.ssblur.unfocused.extension.BlockEntityTypeExtension.create
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

open class ChalkBlock: Block, EntityBlock {
  constructor(): super(
    Properties.of()
      .instabreak()
      .noLootTable()
      .sound(SoundType.STONE)
      .noCollission()
  )

  constructor(properties: Properties): super(properties)

  public override fun getShape(
    blockState: BlockState,
    blockGetter: BlockGetter,
    blockPos: BlockPos,
    collisionContext: CollisionContext
  ): VoxelShape {
    return Shapes.box(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0)
  }

  override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity? {
    return ScriptorBlockEntities.CHALK.create(blockPos, blockState)
  }

  override fun useItemOn(
    itemStack: ItemStack,
    blockState: BlockState,
    level: Level,
    blockPos: BlockPos,
    player: Player,
    interactionHand: InteractionHand,
    blockHitResult: BlockHitResult
  ): ItemInteractionResult? {
    val blockEntity = level.getBlockEntity(blockPos)
    if (blockEntity !is ChalkBlockEntity)
      return super.useItemOn(itemStack, blockState, level, blockPos, player, interactionHand, blockHitResult)

    if(itemStack.`is`(ScriptorItems.DICTIONARY.get())) {
      val data = itemStack[ScriptorDataComponents.DICTIONARY_DATA] ?: DictionaryData(listOf())
      itemStack[ScriptorDataComponents.DICTIONARY_DATA] = data.withWord(blockEntity.word)
      return ItemInteractionResult.SUCCESS
    }
    return super.useItemOn(itemStack, blockState, level, blockPos, player, interactionHand, blockHitResult)
  }

  public override fun useWithoutItem(
    blockState: BlockState,
    level: Level,
    blockPos: BlockPos,
    player: Player,
    blockHitResult: BlockHitResult
  ): InteractionResult {
    val blockEntity = level.getBlockEntity(blockPos)
    if (blockEntity is ChalkBlockEntity) {
      blockEntity.cast()
      return InteractionResult.SUCCESS
    }
    return super.useWithoutItem(blockState, level, blockPos, player, blockHitResult)
  }
}
