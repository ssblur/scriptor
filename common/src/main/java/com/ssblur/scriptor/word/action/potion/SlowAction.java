package com.ssblur.scriptor.word.action.potion;

import net.minecraft.world.effect.MobEffects;

public class SlowAction extends PotionAction {

  public SlowAction() {
    super(MobEffects.MOVEMENT_SLOWDOWN, 80, 1d/3d);
  }

  @Override
  public Cost cost() { return new Cost(6, COSTTYPE.ADDITIVE); }
}
