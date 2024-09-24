package com.ssblur.scriptor.word.action.world.block;

import com.ssblur.scriptor.api.word.Action;
import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluids;

import java.util.List;

public class CreateWaterAction extends Action {
  public static final List<Property<Boolean>> WATERLOGGED = List.of(
    CrossCollisionBlock.WATERLOGGED
  );

  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    var level = targetable.getLevel();
    var pos = targetable.getTargetBlockPos();

    for(var property: WATERLOGGED)
      if(level.getBlockState(pos).hasProperty(property)) {
        level.setBlockAndUpdate(
          pos,
          level.getBlockState(pos).setValue(property, true)
        );
        return;
      }


    if(level.getBlockState(pos).getBlock() == Blocks.CAULDRON) {
      level.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState());
    } else if(level.getBlockState(pos).canBeReplaced(Fluids.WATER)) {
      level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
    }
  }

  @Override
  public Cost cost() {
    return Cost.add(2.0d);
  }
}
