package com.ssblur.scriptor.word.action.potions;

import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.api.word.Word;
import com.ssblur.scriptor.events.network.client.ParticleNetwork;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.CropBlock;

import java.util.Random;

public class WitherAction extends PotionAction {
  static final Random RANDOM = new Random();
  public WitherAction() {
    super(MobEffects.WITHER, 30, 1d/3d, new Word.Cost(12, Word.COSTTYPE.ADDITIVE));
  }

  @Override
  public void applyToPosition(Targetable caster, Targetable targetable, Descriptor[] descriptors, double strength, double duration) {
    boolean playAnimation = false;

    var level = targetable.getLevel();
    var pos = targetable.getOffsetBlockPos();

    if(level.getBlockState(pos).hasProperty(CropBlock.AGE)) {
      var state = level.getBlockState(pos);
      int age = state.getValue(CropBlock.AGE);
      if(age > 0) {
        playAnimation = true;
        for(int i = 0; i < strength + 1; i++)
          age = Math.max(age - (RANDOM.nextInt(2) + 1), 0);
        level.setBlockAndUpdate(pos, state.setValue(CropBlock.AGE, age));
      }
    }

    if(playAnimation) ParticleNetwork.wither(level, pos);
  }
}
