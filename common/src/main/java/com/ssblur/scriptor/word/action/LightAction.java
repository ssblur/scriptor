package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.block.ScriptorBlocks;
import com.ssblur.scriptor.blockentity.LightBlockEntity;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.InventoryTargetable;
import com.ssblur.scriptor.helpers.targetable.ItemTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.duration.DurationDescriptor;
import com.ssblur.scriptor.word.descriptor.visual.ColorDescriptor;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;

public class LightAction extends Action {
  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    int color = 0, colorN = 0;
    for(Descriptor d: descriptors)
      if(d instanceof ColorDescriptor descriptor) {
        color = descriptor.getColor();
        colorN++;
      }
    if(colorN == 0) color = 0xa020f0;
    else color /= colorN;


    BlockPos pos = targetable.getTargetBlockPos();
    Level level = targetable.getLevel();

    if(!level.getBlockState(pos).getMaterial().isReplaceable())
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
