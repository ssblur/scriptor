package com.ssblur.scriptor.block;

import com.ssblur.scriptor.blockentity.GenerateBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

public class GenerateBlock extends Block implements EntityBlock {
  public enum Feature implements StringRepresentable {
    NONE("none"),
    ENGRAVING("engraving");

    String name;
    Feature(String name) {
      this.name = name;
    }


    @Override
    public String getSerializedName() {
      return this.name;
    }
  }

  public static final EnumProperty<Feature> FEATURE = EnumProperty.create("feature", Feature.class);


  public GenerateBlock() {
    super(Properties.of().air());

    this.registerDefaultState(this.stateDefinition.any().setValue(FEATURE, Feature.NONE));
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
    return new GenerateBlockEntity(blockPos, blockState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
    return GenerateBlockEntity::tick;
  }

  public static BlockState generateEngraving() {
    return ScriptorBlocks.GENERATE.get().defaultBlockState().setValue(FEATURE, Feature.ENGRAVING);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FEATURE);
  }
}
