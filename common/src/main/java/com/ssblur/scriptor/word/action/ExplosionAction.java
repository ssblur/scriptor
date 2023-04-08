package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.power.StrengthDescriptor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Explosion;

public class ExplosionAction extends Action {
  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    if(targetable.getLevel().isClientSide) return;
    int strength = 2;
    for(var d: descriptors) {
      if(d instanceof StrengthDescriptor strengthDescriptor)
        strength += strengthDescriptor.strengthModifier();
    }

    ServerLevel level = (ServerLevel) targetable.getLevel();
    var pos = targetable.getTargetPos();
    level.explode(null, pos.x, pos.y + .25, pos.z, strength, Explosion.BlockInteraction.DESTROY);
  }

  @Override
  public Cost cost() { return new Cost(16, COSTTYPE.ADDITIVE); }

}
