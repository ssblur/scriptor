package com.ssblur.scriptor.word.descriptor;

import com.ssblur.scriptor.helpers.targetable.Targetable;

public interface CastDescriptor {
  boolean cannotCast(Targetable caster);
}
