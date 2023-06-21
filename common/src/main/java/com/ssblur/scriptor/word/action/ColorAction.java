package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.color.CustomColors;
import com.ssblur.scriptor.color.interfaces.ColorableBlock;
import com.ssblur.scriptor.helpers.ItemTargetableHelper;
import com.ssblur.scriptor.color.interfaces.Colorable;
import com.ssblur.scriptor.color.interfaces.ColorableItem;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.registry.colorable.ColorableBlockRegistry;
import com.ssblur.scriptor.word.descriptor.Descriptor;

public class ColorAction extends Action {
  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    int color = CustomColors.getColor(descriptors);
    if(targetable instanceof EntityTargetable entityTargetable) {
      if(entityTargetable.getTargetEntity() instanceof Colorable colorable) {
        colorable.setColor(color);
      }
    }

    var itemTarget = ItemTargetableHelper.getTargetItemStack(targetable);
    if(!itemTarget.isEmpty()) {
      if(itemTarget.getItem() instanceof ColorableItem colorable) {
        var itemOut = colorable.setColor(color, itemTarget);
        if(!itemOut.isEmpty()) {
          itemTarget.setCount(0);
          ItemTargetableHelper.depositItemStack(targetable, itemOut);
        }
      }
      return;
    }

    var blockEntity = targetable.getLevel().getBlockEntity(
      targetable.getOffsetBlockPos()
    );
    if(blockEntity instanceof Colorable colorable) {
      colorable.setColor(color);
      return;
    }

    var block = targetable.getLevel().getBlockState(targetable.getOffsetBlockPos()).getBlock();
    if(block instanceof ColorableBlock colorable) {
      colorable.setColor(color, targetable.getLevel(), targetable.getOffsetBlockPos());
    } else if(ColorableBlockRegistry.has(block)) {
      ColorableBlockRegistry.get(block).setColor(color, targetable.getLevel(), targetable.getOffsetBlockPos());
    }
  }

  @Override
  public Cost cost() { return new Cost(8, COSTTYPE.ADDITIVE); }

}
