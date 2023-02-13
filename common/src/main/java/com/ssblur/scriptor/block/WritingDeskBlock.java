package com.ssblur.scriptor.block;

import com.ssblur.scriptor.blockentity.RuneBlockEntity;
import com.ssblur.scriptor.blockentity.WritingDeskBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WritingDeskBlock extends Block implements EntityBlock {
  public enum BinderColor implements StringRepresentable {
    BROWN,
    PURPLE;

    @Override
    public String getSerializedName() {
      return this.name().toLowerCase();
    }
  }
  static EnumProperty<BinderColor> BinderColorProperty = EnumProperty.create("binder", BinderColor.class);

  public WritingDeskBlock() {
    super(
      Properties
        .of(Material.WOOD)
        .sound(SoundType.WOOD)
        .noOcclusion()
    );

    this.registerDefaultState(
        this.defaultBlockState()
          .setValue(BinderColorProperty, BinderColor.BROWN)
          .setValue(ScriptorBlocks.HorizontalProperty, ScriptorBlocks.HorizontalFacing.NORTH)
    );
  }

  public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
    return this.defaultBlockState().setValue(
      ScriptorBlocks.HorizontalProperty,
      ScriptorBlocks.HorizontalFacing.of(blockPlaceContext.getHorizontalDirection().getOpposite())
    );
  }

  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(BinderColorProperty, ScriptorBlocks.HorizontalProperty);
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
    return Shapes.box(0, 0, 0, 1, 0.75, 1);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new WritingDeskBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> GameEventListener getListener(ServerLevel serverLevel, T blockEntity) {
    return EntityBlock.super.getListener(serverLevel, blockEntity);
  }
}
