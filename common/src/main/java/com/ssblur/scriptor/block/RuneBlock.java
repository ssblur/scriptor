package com.ssblur.scriptor.block;

import com.google.common.collect.ImmutableMap;
import com.ssblur.scriptor.blockentity.RuneBlockEntity;
import com.ssblur.scriptor.blockentity.ScriptorBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Function;

public class RuneBlock extends Block implements EntityBlock {
  public RuneBlock() {
    super(
      Properties.of()
        .instabreak()
        .noLootTable()
        .sound(SoundType.AMETHYST)
        .noCollission()
    );
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
    return Shapes.box(0, 0, 0, 1, 0.0625, 1);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new RuneBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
    return RuneBlockEntity::tick;
  }

  @Nullable
  @Override
  public <T extends BlockEntity> GameEventListener getListener(ServerLevel serverLevel, T blockEntity) {
    return EntityBlock.super.getListener(serverLevel, blockEntity);
  }
}
