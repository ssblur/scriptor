package com.ssblur.scriptor.block;

import com.ssblur.scriptor.blockentity.CastingLecternBlockEntity;
import com.ssblur.scriptor.blockentity.ChalkBlockEntity;
import com.ssblur.scriptor.blockentity.EngravingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class EngravingBlock extends ChalkBlock {
  public EngravingBlock() {
    super(
      Properties.of()
        .destroyTime(5f)
        .noLootTable()
        .sound(SoundType.STONE)
        .noCollission()
    );
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
}
