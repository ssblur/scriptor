package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class SwapAction extends Action {
  @Override
  public void apply(Entity caster, Targetable targetable, Descriptor[] descriptors) {
    if(targetable.getLevel().isClientSide
        || caster == null
        || caster.level == null
        || targetable.getLevel() == null) return;

    var level = (ServerLevel) targetable.getLevel();
    var casterLevel = (ServerLevel) caster.level;
    var pos = targetable.getTargetPos();
    var casterPos = caster.position();
    if(caster.level != level)
      caster.changeDimension(level);
    caster.teleportTo(pos.x, pos.y, pos.z);
    caster.setDeltaMovement(0, 0, 0);
    caster.resetFallDistance();
    if(targetable instanceof EntityTargetable entityTargetable)
      if (entityTargetable.getTargetEntity() instanceof LivingEntity living) {
        if(living.level != casterLevel)
          living.changeDimension(casterLevel);
        living.teleportTo(casterPos.x, casterPos.y, casterPos.z);
        living.setDeltaMovement(0, 0, 0);
        living.resetFallDistance();
      }
  }

  @Override
  public Cost cost() { return new Cost(6, COSTTYPE.ADDITIVE); }

}
