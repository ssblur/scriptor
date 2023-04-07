package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.ItemTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;

public class BringAction extends Action {
  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    if(targetable.getLevel().isClientSide) return;

    ServerLevel level = (ServerLevel) targetable.getLevel();
    var pos = targetable.getTargetPos();
    if(caster != null) {
      var casterPos = caster.getTargetPos();
      if(targetable instanceof ItemTargetable itemTargetable && itemTargetable.shouldTargetItem()) {
        var item = itemTargetable.getTargetItem();
        if(item != null && !item.isEmpty()) {
          var newItem = item.copy();
          newItem.setCount(1);
          ItemEntity entity = new ItemEntity(caster.getLevel(), pos.x(), pos.y() + 1, pos.z(), newItem);
          caster.getLevel().addFreshEntity(entity);
          item.shrink(1);
          return;
        }
      }
      if(targetable instanceof EntityTargetable entityTargetable)
        if(entityTargetable.getTargetEntity() instanceof LivingEntity living) {
          if(caster.getLevel() != level)
            living.changeDimension((ServerLevel) caster.getLevel());
          living.teleportTo(casterPos.x, casterPos.y, casterPos.z);
          living.setDeltaMovement(0, 0, 0);
          living.resetFallDistance();
        }
    }
  }

  @Override
  public Cost cost() { return new Cost(6, COSTTYPE.ADDITIVE); }

}
