package com.ssblur.scriptor.block;

import com.ssblur.scriptor.blockentity.EngravingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public class EngravingBlock extends ChalkBlock {
  public static final BooleanProperty HIGHLIGHT = BooleanProperty.create("highlight");


  public EngravingBlock() {
    super(
      Properties.of()
        .destroyTime(5f)
        .noLootTable()
        .sound(SoundType.STONE)
        .noCollission()
    );

    this.registerDefaultState(this.stateDefinition.any().setValue(HIGHLIGHT, false));
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new EngravingBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
    return EngravingBlockEntity::tick;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(HIGHLIGHT);
  }
}
