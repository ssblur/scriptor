package com.ssblur.scriptor.word.action.potions;

import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.blockentity.PhasedBlockBlockEntity;
import com.ssblur.scriptor.effect.ScriptorEffects;
import com.ssblur.scriptor.helpers.targetable.Targetable;

public class WildPhasingAction extends PotionAction {
  public WildPhasingAction() {
    super(ScriptorEffects.WILD_PHASING.get(), 20, 1d/3d, new Cost(15, COSTTYPE.ADDITIVE));
  }

  @Override
  public void applyToPosition(Targetable caster, Targetable targetable, Descriptor[] descriptors, double strength, double duration) {
    var level = targetable.getLevel();
    var pos = targetable.getOffsetBlockPos();

    PhasedBlockBlockEntity.phase(level, pos);
  }
}
