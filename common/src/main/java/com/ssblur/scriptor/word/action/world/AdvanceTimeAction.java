package com.ssblur.scriptor.word.action.world;

import com.ssblur.scriptor.api.word.Action;
import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.api.word.descriptor.StrengthDescriptor;
import net.minecraft.server.level.ServerLevel;

public class AdvanceTimeAction extends Action {
  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    if(targetable.getLevel().isClientSide) return;
    double strength = 2;
    for(var d: descriptors) {
      if(d instanceof StrengthDescriptor strengthDescriptor)
        strength += strengthDescriptor.strengthModifier();
    }

    var serverLevel = (ServerLevel) targetable.getLevel();
    serverLevel.setDayTime(serverLevel.getDayTime() + (long) (1000 * strength));
  }
  @Override
  public Cost cost() { return new Cost(52, COSTTYPE.ADDITIVE); }
}
