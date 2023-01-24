package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.Word;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import net.minecraft.world.entity.Entity;

public class HoldAction extends Action {
  @Override
  public Cost cost() {
    return new Cost(4, COSTTYPE.ADDITIVE);
  }

  @Override
  public void apply(Entity caster, Targetable targetable, Descriptor[] descriptors) {
    // Pull entity towards caster
    // Position is the same angle as original, *radius* blocks away
    // After spell duration expires, target is flung away from user with an angle
    // based on caster's pitch
  }
}
