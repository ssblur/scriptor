package com.ssblur.scriptor.word.descriptor;

import com.ssblur.scriptor.helpers.targetable.Targetable;
import net.minecraft.world.entity.Entity;

public interface CastDescriptor {
  boolean cannotCast(Targetable caster);
}
