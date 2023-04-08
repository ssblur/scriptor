package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.StrengthDescriptor;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;

public class HarmAction extends Action {
  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    double strength = 1;
    for(var d: descriptors) {
      if(d instanceof StrengthDescriptor strengthDescriptor)
        strength += strengthDescriptor.strengthModifier();
    }

    strength = Math.max(strength, 0);
    strength *= 2;

    if(targetable instanceof EntityTargetable entityTargetable) {
      Entity entity = entityTargetable.getTargetEntity();
      Entity source = caster instanceof EntityTargetable casterEntity ? casterEntity.getTargetEntity() : entity;
      if(entity instanceof LivingEntity target)
        if(target.getMobType() == MobType.UNDEAD)
          target.heal((float) strength);
        else
          target.hurt(DamageSource.indirectMagic(source, source), (float) strength);
    }
  }
  @Override
  public Cost cost() { return new Cost(3, COSTTYPE.ADDITIVE); }
}
