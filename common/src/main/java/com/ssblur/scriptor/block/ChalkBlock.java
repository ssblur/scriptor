package com.ssblur.scriptor.block;

import com.ssblur.scriptor.blockentity.ChalkBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ChalkBlock extends Block implements EntityBlock {
  public ChalkBlock() {
    super(
      Properties.of()
        .instabreak()
        .noLootTable()
        .sound(SoundType.STONE)
        .noCollission()
    );
  }

  public ChalkBlock(Properties properties) {
    super(properties);
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
    return Shapes.box(0, 0, 0, 1, 0.0625, 1);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new ChalkBlockEntity(blockPos, blockState);
  }

  @Override
  public InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
    if(level.getBlockEntity(blockPos) instanceof ChalkBlockEntity blockEntity) {
      blockEntity.cast();
      return InteractionResult.SUCCESS;
    }
    return super.useWithoutItem(blockState, level, blockPos, player, blockHitResult);
  }
}
