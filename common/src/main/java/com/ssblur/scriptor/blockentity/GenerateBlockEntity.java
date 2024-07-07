package com.ssblur.scriptor.blockentity;

import com.ssblur.scriptor.block.EngravingBlock;
import com.ssblur.scriptor.block.GenerateBlock;
import com.ssblur.scriptor.block.ScriptorBlocks;
import com.ssblur.scriptor.data.DictionarySavedData;
import com.ssblur.scriptor.events.reloadlisteners.EngravingReloadListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class GenerateBlockEntity extends BlockEntity {
  public GenerateBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(ScriptorBlockEntities.GENERATE.get(), blockPos, blockState);
  }

  static void generateEngraving(ServerLevel level, BlockPos pos) {
    Random random = new Random();

    var engraving = EngravingReloadListener.INSTANCE.getRandomEngraving();
    String[] words = DictionarySavedData.computeIfAbsent(level)
      .generate(engraving.getSpell())
      .split(" ");
    Direction dir = Direction.fromAxisAndDirection(
      random.nextBoolean() ? Direction.Axis.X : Direction.Axis.Z,
      random.nextBoolean() ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE
    );
    Direction last = dir;
    var curPos = pos;
    boolean forward = false;
    boolean first = true;

    for(var word: words) {
      if(forward) {
        engrave(level, curPos, word, false);
        curPos = pos.relative(dir);
        forward = false;
        continue;
      }

      if(last == dir) {
        switch(random.nextInt(3)) {
          case 0 -> last = dir;
          case 1 -> last = dir.getClockWise();
          case 2 -> last = dir.getCounterClockWise();
        }
      } else if(random.nextBoolean()) {
        last = dir;
        forward = true;
      }

      engrave(level, curPos, word, first);
      first = false;
      curPos = curPos.relative(last);
    }
  }

  static void engrave(ServerLevel level, BlockPos pos, String word, boolean bold) {
    if(!level.getBlockState(pos.offset(0, -1, 0)).isSolid())
      level.setBlockAndUpdate(pos.offset(0, -1, 0), Blocks.STONE.defaultBlockState());

    var blockState = ScriptorBlocks.ENGRAVING.get().defaultBlockState();
    if(bold)
      blockState = blockState.setValue(EngravingBlock.HIGHLIGHT, true);
    level.setBlockAndUpdate(pos, blockState);
    var entity = new EngravingBlockEntity(pos, blockState);
    entity.setWord(word);
    level.setBlockEntity(entity);
  }

  public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T entity) {
    if(level.isClientSide) return;

    if (state.getValue(GenerateBlock.FEATURE) == GenerateBlock.Feature.ENGRAVING) {
      generateEngraving((ServerLevel) level, pos);
    }
  }
}
