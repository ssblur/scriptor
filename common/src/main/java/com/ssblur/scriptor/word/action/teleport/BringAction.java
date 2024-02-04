package com.ssblur.scriptor.word.action.teleport;

import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.api.word.Descriptor;

public class BringAction extends SwapAction {
  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    if(targetable.getLevel().isClientSide
      || caster == null
      || caster.getLevel() == null
      || targetable.getLevel() == null) return;

    teleport(targetable, caster);
  }

  @Override
  public Cost cost() { return new Cost(6, COSTTYPE.ADDITIVE); }

}
