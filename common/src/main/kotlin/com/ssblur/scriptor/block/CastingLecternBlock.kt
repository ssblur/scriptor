package com.ssblur.scriptor.block

import com.ssblur.scriptor.blockentity.CastingLecternBlockEntity
import com.ssblur.scriptor.item.books.Spellbook
import com.ssblur.scriptor.item.casters.CasterCrystal
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes

class CastingLecternBlock : Block(Properties.ofFullCopy(Blocks.ACACIA_PLANKS).noOcclusion()), EntityBlock {
    init {
        this.registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH))
    }

    public override fun useItemOn(
        itemStack: ItemStack,
        blockState: BlockState,
        level: Level,
        blockPos: BlockPos,
        player: Player,
        interactionHand: InteractionHand,
        blockHitResult: BlockHitResult
    ): ItemInteractionResult {
        val blockEntity = level.getBlockEntity(blockPos)

        if (!level.isClientSide && blockEntity is CastingLecternBlockEntity) if (itemStack.isEmpty) {
            if (!blockEntity.spellbook.isEmpty) {
                player.setItemInHand(interactionHand, blockEntity.spellbook)
                blockEntity.spellbook = itemStack
            } else {
                player.setItemInHand(interactionHand, blockEntity.focus)
                blockEntity.focus = itemStack
            }
        } else if (itemStack.item is Spellbook) {
            player.setItemInHand(interactionHand, blockEntity.spellbook)
            blockEntity.spellbook = itemStack
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
        } else if (itemStack.item is CasterCrystal) {
            player.setItemInHand(interactionHand, blockEntity.focus)
            blockEntity.focus = itemStack
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
        }
        return ItemInteractionResult.CONSUME
    }

    override fun getStateForPlacement(blockPlaceContext: BlockPlaceContext) =
        defaultBlockState().setValue(FACING, blockPlaceContext.horizontalDirection.opposite)

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    override fun newBlockEntity(blockPos: BlockPos, blockState: BlockState): BlockEntity {
        return CastingLecternBlockEntity(blockPos, blockState)
    }

    override fun <T : BlockEntity> getTicker(
        level: Level,
        blockState: BlockState,
        blockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T> = BlockEntityTicker {
        tickerLevel, pos: BlockPos?, state: BlockState?, entity -> CastingLecternBlockEntity.tick(tickerLevel, pos, state, entity as BlockEntity)
    }

    public override fun getShape(
        blockState: BlockState,
        blockGetter: BlockGetter,
        blockPos: BlockPos,
        collisionContext: CollisionContext
    ) = Shapes.box(0.0625, 0.0, 0.0625, 0.875, 0.9375, 0.875)

    override fun onRemove(
        blockState: BlockState,
        level: Level,
        blockPos: BlockPos,
        blockState2: BlockState,
        bl: Boolean
    ) {
        if (!level.isClientSide) {
            if (level.getBlockEntity(blockPos) is CastingLecternBlockEntity) {
                val lectern = level.getBlockEntity(blockPos) as CastingLecternBlockEntity
                for (item in lectern.items) {
                    val entity = ItemEntity(
                        level,
                        (blockPos.x + 0.5f).toDouble(),
                        (blockPos.y + 0.5f).toDouble(),
                        (blockPos.z + 0.5f).toDouble(),
                        item
                    )
                    level.addFreshEntity(entity)
                }
            }
        }
        super.onRemove(blockState, level, blockPos, blockState2, bl)
    }

    companion object {
        @JvmField
        val FACING: DirectionProperty = HorizontalDirectionalBlock.FACING
    }
}
