package com.ssblur.scriptor.api.word;

import com.ssblur.scriptor.helpers.targetable.Targetable;


public abstract class Action extends Word {
  /**
   * Applies the effects of this spell.
   * This step should factor in any Descriptors on this spell.
   * @param caster The Entity which cast this spell
   * @param targetable A Targetable which describes the target of this spell (position, entity, item, etc.)
   * @param descriptors A list of all Descriptors which this spell contains
   */
  public abstract void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors);
}
