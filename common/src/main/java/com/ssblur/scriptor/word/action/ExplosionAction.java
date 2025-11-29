package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.api.word.Action;
import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.power.StrengthDescriptor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class ExplosionAction extends Action {
  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    if(targetable.getLevel().isClientSide) return;
    double strength = 2;
    for(var d: descriptors) {
      if(d instanceof StrengthDescriptor strengthDescriptor)
        strength += (float) strengthDescriptor.strengthModifier();
    }

    ServerLevel level = (ServerLevel) targetable.getLevel();
    var pos = targetable.getTargetPos();

    float power = (float) Math.pow(strength, 0.7);

    level.explode(null, pos.x, pos.y + .25, pos.z, power, Level.ExplosionInteraction.TNT);
  }

  @Override
  public Cost cost() { return new Cost(16, COSTTYPE.ADDITIVE); }

}
