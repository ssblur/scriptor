package com.ssblur.scriptor.word.action.potions;

import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.blockentity.PhasedBlockBlockEntity;
import com.ssblur.scriptor.blockentity.renderers.PhasedBlockBlockEntityRenderer;
import com.ssblur.scriptor.effect.PhasingStatusEffect;
import com.ssblur.scriptor.effect.ScriptorEffects;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;

public class PhasingAction extends PotionAction {
  public PhasingAction() {
    super(ScriptorEffects.PHASING.get(), 20, 1d/3d, new Cost(3, COSTTYPE.ADDITIVE));
  }

  @Override
  public void applyToPosition(Targetable caster, Targetable targetable, Descriptor[] descriptors, double strength, double duration) {
    var level = targetable.getLevel();
    var pos = targetable.getOffsetBlockPos();

    PhasedBlockBlockEntity.phase(level, pos, (int) Math.floor(duration * 3));
  }
}
