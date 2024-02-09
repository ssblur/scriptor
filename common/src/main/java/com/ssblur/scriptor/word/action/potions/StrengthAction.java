package com.ssblur.scriptor.word.action.potions;

import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.api.word.Word;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;

public class StrengthAction extends PotionAction {
  public StrengthAction() {
    super(MobEffects.DAMAGE_BOOST, 40, 1d/3d, new Word.Cost(9, Word.COSTTYPE.ADDITIVE));
  }

  @Override
  public void applyToPosition(Targetable caster, Targetable targetable, Descriptor[] descriptors, double strength, double duration) {
    boolean playBoneMealAnimation = false;

    var level = targetable.getLevel();
    var pos = targetable.getOffsetBlockPos();

    for(int i = 0; i < strength + 1; i++)
      if(
        BoneMealItem.growCrop(ItemStack.EMPTY, level, pos)
          || BoneMealItem.growWaterPlant(ItemStack.EMPTY, level, pos, targetable.getFacing().getOpposite())
      )
        playBoneMealAnimation = true;

    if(playBoneMealAnimation) level.levelEvent(1505, pos, 0);
  }
}
