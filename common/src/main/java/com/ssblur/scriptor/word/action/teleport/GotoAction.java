package com.ssblur.scriptor.word.action.teleport;

import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.ItemTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.action.Action;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;

public class GotoAction extends Action {
  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    if(targetable.getLevel().isClientSide) return;

    ServerLevel level = (ServerLevel) targetable.getLevel();
    var pos = targetable.getTargetPos();

    if(caster instanceof ItemTargetable itemTargetable && itemTargetable.shouldTargetItem()) {
      var item = itemTargetable.getTargetItem();
      if(item != null && !item.isEmpty()) {
        var newItem = item.copy();
        newItem.setCount(1);
        ItemEntity entity = new ItemEntity(targetable.getLevel(), pos.x(), pos.y() + 1, pos.z(), newItem);
        targetable.getLevel().addFreshEntity(entity);
        item.shrink(1);
        return;
      }
    }

    if(caster instanceof EntityTargetable entityTargetable) {
      var entity = entityTargetable.getTargetEntity();
      if (entity.level != level)
        entity.changeDimension(level);
      entity.teleportTo(pos.x, pos.y, pos.z);
      entity.setDeltaMovement(0, 0, 0);
      entity.resetFallDistance();
    }
  }

  @Override
  public Cost cost() { return new Cost(6, COSTTYPE.ADDITIVE); }

}
