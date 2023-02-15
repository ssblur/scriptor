package com.ssblur.scriptor.block;

import com.ssblur.scriptor.block.blockstates.HorizontalFacing;
import com.ssblur.scriptor.blockentity.WritingDeskBlockEntity;
import com.ssblur.scriptor.container.ScriptorContainers;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WritingDeskBlock extends Block implements EntityBlock {
  public enum BinderColor implements StringRepresentable {
    NONE,
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
          .setValue(ScriptorBlocks.HorizontalProperty, HorizontalFacing.NORTH)
    );
  }

  public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
    return this.defaultBlockState().setValue(
      ScriptorBlocks.HorizontalProperty,
      HorizontalFacing.forFacing(blockPlaceContext)
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

  @Override
  public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
    if(level.isClientSide) return InteractionResult.SUCCESS;

    if(level.getBlockEntity(blockPos) instanceof ExtendedMenuProvider provider)
      MenuRegistry.openExtendedMenu((ServerPlayer) player, provider);
    return InteractionResult.SUCCESS;
  }
}
