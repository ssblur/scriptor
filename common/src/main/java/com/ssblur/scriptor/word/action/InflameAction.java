package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.DurationDescriptor;
import net.minecraft.world.entity.Entity;

public class InflameAction extends Action {
  @Override
  public void apply(Entity caster, Targetable targetable, Descriptor[] descriptors) {
    int seconds = 2;
    for(var d: descriptors) {
      if(d instanceof DurationDescriptor)
        seconds++;
    }

    if(targetable instanceof EntityTargetable entityTargetable) {
      entityTargetable.getTargetEntity().setSecondsOnFire(seconds);
    }
  }

  @Override
  public int cost() {
    return 1;
  }
}
