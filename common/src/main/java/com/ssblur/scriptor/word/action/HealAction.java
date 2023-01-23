package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.DurationDescriptor;
import com.ssblur.scriptor.word.descriptor.ExtensionDescriptor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class HealAction extends Action {
  @Override
  public void apply(Entity caster, Targetable targetable, Descriptor[] descriptors) {
    int strength = 2;
    for(var d: descriptors) {
      if(d instanceof ExtensionDescriptor)
        strength++;
    }

    if(targetable instanceof EntityTargetable entityTargetable) {
      Entity entity = entityTargetable.getTargetEntity();
      if(entity instanceof LivingEntity)
        ((LivingEntity) entity).heal(strength);
    }
  }
  @Override
  public int cost() {
    return 2;
  }
}
