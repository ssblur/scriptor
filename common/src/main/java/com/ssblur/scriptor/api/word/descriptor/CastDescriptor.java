package com.ssblur.scriptor.api.word.descriptor;

import com.ssblur.scriptor.helpers.targetable.Targetable;

public interface CastDescriptor {
  /**
   * A method which determines if this spell can be cast.
   * @param caster A Targetable representing the casting player, lectern, chalk, etc.
   * @return true to prevent casting
   */
  boolean cannotCast(Targetable caster);
}
