package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.DurationDescriptor;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class InflameAction extends Action {
  @Override
  public void apply(Entity caster, Targetable targetable, Descriptor[] descriptors) {
    double seconds = 2;
    for(var d: descriptors) {
      if(d instanceof DurationDescriptor durationDescriptor)
        seconds += durationDescriptor.durationModifier();
    }

    if(targetable instanceof EntityTargetable entityTargetable) {
      entityTargetable.getTargetEntity().setSecondsOnFire((int) Math.round(seconds));
    } else {
      BlockPos pos = targetable.getTargetBlockPos();
      Level level = targetable.getLevel();

      if(!level.getBlockState(pos).getMaterial().isReplaceable())
        return;

      BlockState blockState2 = BaseFireBlock.getState(level, pos);
      level.setBlock(pos, blockState2, 11);

      if(caster instanceof Player player)
        level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
      else
        level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
    }
  }

  @Override
  public Cost cost() { return new Cost(2, COSTTYPE.ADDITIVE); }

}
