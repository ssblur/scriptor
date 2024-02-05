package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.api.word.Action;
import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.color.CustomColors;
import com.ssblur.scriptor.helpers.ItemTargetableHelper;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.registry.colorable.ColorableBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;

public class PlaceBlockAction extends Action {
  @Override
  public Cost cost() {
    return new Cost(1.5d, COSTTYPE.ADDITIVE);
  }

  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    var color = CustomColors.getColor(descriptors);
    BlockPos pos = targetable.getTargetBlockPos();
    Level level = targetable.getLevel();

    if(!level.getBlockState(pos).canBeReplaced())
      return;

    var itemFocus = ItemTargetableHelper.getTargetItemStack(
      caster,
      false,
      itemStack -> itemStack.getItem() instanceof BlockItem
    );

    if(itemFocus.getItem() instanceof BlockItem blockItem) {
      itemFocus.shrink(1);
      var block = blockItem.getBlock();
      level.setBlock(pos, block.defaultBlockState(), 11);
    } else {
      ColorableBlockRegistry.DYE_COLORABLE_BLOCKS.MAGIC_BLOCK.setColor(color, level, pos);
    }

  }
}
