package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.ItemTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.DurationDescriptor;
import com.ssblur.scriptor.word.descriptor.StrengthDescriptor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class HealAction extends Action {
  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    double strength = 2;
    for(var d: descriptors) {
      if(d instanceof StrengthDescriptor strengthDescriptor)
        strength += strengthDescriptor.strengthModifier();
    }

    if(targetable instanceof ItemTargetable itemTargetable) {
      var item = itemTargetable.getTargetItem();
      if(item != null && !item.isEmpty()) {
        if(item.isDamageableItem()) {
          item.setDamageValue(item.getDamageValue() - (int) Math.round(strength));
          return;
        }
      }
    }

    if(targetable instanceof EntityTargetable entityTargetable) {
      Entity entity = entityTargetable.getTargetEntity();
      if(entity instanceof LivingEntity target)
        target.heal((int) Math.round(strength));
    }
  }
  @Override
  public Cost cost() { return new Cost(4, COSTTYPE.ADDITIVE); }
}
