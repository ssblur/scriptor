package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.api.word.Action;
import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.block.ScriptorBlocks;
import com.ssblur.scriptor.blockentity.LightBlockEntity;
import com.ssblur.scriptor.color.CustomColors;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.api.word.descriptor.DurationDescriptor;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class LightAction extends Action {
  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    int seconds = 6;
    for(var d: descriptors) {
      if(d instanceof DurationDescriptor durationDescriptor)
        seconds += 3 * durationDescriptor.durationModifier();
    }

    if(targetable instanceof EntityTargetable entityTargetable && entityTargetable.getTargetEntity() instanceof LivingEntity living) {
      living.addEffect(new MobEffectInstance(MobEffects.GLOWING, seconds));
      return;
    }

    int color = CustomColors.getColor(descriptors);
    BlockPos pos = targetable.getTargetBlockPos();
    Level level = targetable.getLevel();

    if(!level.getBlockState(pos).canBeReplaced())
      return;

    BlockState blockState2 = ScriptorBlocks.LIGHT.get().defaultBlockState();
    level.setBlock(pos, blockState2, 11);

    var blockEntity = level.getBlockEntity(pos);
    if(blockEntity instanceof LightBlockEntity lightBlockEntity)
      lightBlockEntity.setColor(color);

    level.playSound(null, pos, SoundEvents.WOOL_PLACE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
  }

  @Override
  public Cost cost() { return new Cost(2, COSTTYPE.ADDITIVE); }
}
