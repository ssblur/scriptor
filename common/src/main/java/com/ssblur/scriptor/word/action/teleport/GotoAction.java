package com.ssblur.scriptor.word.action.teleport;

import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.ItemTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.action.Action;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;

public class GotoAction extends SwapAction {
  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    if(targetable.getLevel().isClientSide
      || caster == null
      || caster.getLevel() == null
      || targetable.getLevel() == null) return;

    teleport(caster, targetable);
  }

  @Override
  public Cost cost() { return new Cost(6, COSTTYPE.ADDITIVE); }
}
