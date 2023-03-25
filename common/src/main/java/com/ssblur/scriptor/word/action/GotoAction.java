package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.StrengthDescriptor;
import net.minecraft.server.commands.TeleportCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.level.Explosion;

public class GotoAction extends Action {
  @Override
  public void apply(Entity caster, Targetable targetable, Descriptor[] descriptors) {
    if(targetable.getLevel().isClientSide) return;

    ServerLevel level = (ServerLevel) targetable.getLevel();
    var pos = targetable.getTargetPos();
    if(caster != null) {
      if (caster.level != level)
        caster.changeDimension(level);
      caster.teleportTo(pos.x, pos.y, pos.z);
      caster.setDeltaMovement(0, 0, 0);
      caster.resetFallDistance();
    }
  }

  @Override
  public Cost cost() { return new Cost(6, COSTTYPE.ADDITIVE); }

}
