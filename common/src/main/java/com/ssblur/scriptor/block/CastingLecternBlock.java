package com.ssblur.scriptor.block;

import com.ssblur.scriptor.blockentity.CastingLecternBlockEntity;
import com.ssblur.scriptor.item.Spellbook;
import com.ssblur.scriptor.item.casters.CasterCrystal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class CastingLecternBlock extends Block implements EntityBlock {
  public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
  public CastingLecternBlock() {
    super(
      Properties
        .of(Material.WOOD)
        .sound(SoundType.WOOD)
        .strength(0.2f)
        .noOcclusion()
    );
    this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
  }

  @SuppressWarnings("deprecation")
  @Override
  public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
    BlockEntity blockEntity = level.getBlockEntity(blockPos);
    ItemStack itemStack = player.getItemInHand(interactionHand);

    if(!level.isClientSide && blockEntity instanceof CastingLecternBlockEntity lectern)
      if(itemStack.isEmpty()) {
        if(!lectern.getSpellbook().isEmpty()) {
          player.setItemInHand(interactionHand, lectern.getSpellbook());
          lectern.setSpellbook(itemStack);
        } else {
          player.setItemInHand(interactionHand, lectern.getFocus());
          lectern.setFocus(itemStack);
        }
      } else if (itemStack.getItem() instanceof Spellbook)  {
        player.setItemInHand(interactionHand, lectern.getSpellbook());
        lectern.setSpellbook(itemStack);
        return InteractionResult.PASS;
      } else if (itemStack.getItem() instanceof CasterCrystal)  {
        player.setItemInHand(interactionHand, lectern.getFocus());
        lectern.setFocus(itemStack);
        return InteractionResult.PASS;
      }
    return InteractionResult.CONSUME;
  }

  @Override
  public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
    super.playerWillDestroy(level, blockPos, blockState, player);
    if(level.getBlockEntity(blockPos) instanceof CastingLecternBlockEntity lectern) {
      for(var item: lectern.getItems()) {
        var entity = new ItemEntity(level, blockPos.getX() + 0.5f, blockPos.getY() + 0.5f, blockPos.getZ() + 0.5f, item);
        level.addFreshEntity(entity);
      }
    }
  }

  public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
    return this.defaultBlockState().setValue(FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new CastingLecternBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
    return CastingLecternBlockEntity::tick;
  }

  @Nullable
  @Override
  public <T extends BlockEntity> GameEventListener getListener(ServerLevel serverLevel, T blockEntity) {
    return EntityBlock.super.getListener(serverLevel, blockEntity);
  }
}
