package com.ssblur.scriptor.word.descriptor;

import com.ssblur.scriptor.helpers.targetable.Targetable;

public interface AfterCastDescriptor {
  /**
   * The method to call after casting.
   * This is called after the initial cast, not after the subject is returned.
   * @param caster A Targetable representing this spell's caster
   */
  void afterCast(Targetable caster);
}
