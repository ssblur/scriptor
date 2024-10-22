package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.api.word.Action;
import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.color.CustomColors;
import com.ssblur.scriptor.events.network.client.ParticleNetwork;
import com.ssblur.scriptor.helpers.ItemTargetableHelper;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.mixin.BlockPlaceContextAccessor;
import com.ssblur.scriptor.registry.colorable.ColorableBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

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
      var status = blockItem.place(
        BlockPlaceContextAccessor.createBlockPlaceContext(
          level,
          null,
          InteractionHand.MAIN_HAND,
          itemFocus,
          new BlockHitResult(targetable.getTargetPos(), targetable.getFacing(), targetable.getTargetBlockPos(), false)
        )
      );
      if(!status.consumesAction())
        ParticleNetwork.fizzle(level, targetable.getTargetBlockPos());
    } else {
      ColorableBlockRegistry.DYE_COLORABLE_BLOCKS.MAGIC_BLOCK.setColor(color, level, pos);
    }

  }
}
