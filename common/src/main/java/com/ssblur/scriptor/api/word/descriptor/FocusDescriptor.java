package com.ssblur.scriptor.api.word.descriptor;

import com.ssblur.scriptor.helpers.targetable.Targetable;

public interface FocusDescriptor {
  /**
   * This is called before cast to transform the "self" target or focus
   * of a spell.
   * @param targetable A Targetable representing the caster (or modified focus)
   * @return The transformed Targetable
   */
  Targetable modifyFocus(Targetable targetable);
}
