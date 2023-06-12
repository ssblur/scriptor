package com.ssblur.scriptor.block;

import com.ssblur.scriptor.blockentity.LightBlockEntity;
import com.ssblur.scriptor.blockentity.RuneBlockEntity;
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
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class LightBlock extends Block implements EntityBlock {
  public LightBlock() {
    super(
      Properties
        .of(Material.DECORATION)
        .instabreak()
        .noLootTable()
        .sound(SoundType.WOOL)
        .noCollission()
        .lightLevel(state -> 15)
    );
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
    return Shapes.box(0.25, 0.25, 0.25, 0.75, 0.75, 0.75);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new LightBlockEntity(blockPos, blockState);
  }
}
