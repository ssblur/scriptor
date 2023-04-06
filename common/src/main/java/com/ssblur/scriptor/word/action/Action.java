package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.Word;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import net.minecraft.world.entity.Entity;


public abstract class Action extends Word {
  /**
   * Applies the effects of this spell.
   * This step should factor in any Descriptors on this spell.
   * @param caster The Entity which cast this spell
   * @param targetable A Targetable which describes the target of this spell (position, entity, item, etc.)
   * @param descriptors A list of all Descriptors which this spell contained
   */
  public abstract void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors);
}
