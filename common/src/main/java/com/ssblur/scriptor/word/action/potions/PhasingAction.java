package com.ssblur.scriptor.word.action.potions;

import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.blockentity.PhasedBlockBlockEntity;
import com.ssblur.scriptor.effect.ScriptorEffects;
import com.ssblur.scriptor.helpers.targetable.Targetable;

public class PhasingAction extends PotionAction {
  public PhasingAction() {
    super(ScriptorEffects.get(ScriptorEffects.PHASING), 20, 1d, new Cost(8, COSTTYPE.ADDITIVE));
  }

  @Override
  public void applyToPosition(Targetable caster, Targetable targetable, Descriptor[] descriptors, double strength, double duration) {
    var level = targetable.getLevel();
    var pos = targetable.getOffsetBlockPos();

    PhasedBlockBlockEntity.phase(level, pos, (int) Math.floor(duration * 3));
  }
}
