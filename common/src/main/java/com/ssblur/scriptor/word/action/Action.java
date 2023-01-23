package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import net.minecraft.world.entity.Entity;


public abstract class Action {
  /**
   * Applies the effects of this spell.
   * This step should factor in any Descriptors on this spell.
   * @param caster The Entity which cast this spell
   * @param targetable A Targetable which describes the target of this spell (position, entity, item, etc.)
   * @param descriptors A list of all Descriptors which this spell contained
   */
  public abstract void apply(Entity caster, Targetable targetable, Descriptor[] descriptors);
  /**
   * @return A number representing material cost or cast cooldown.
   * Actions shall cost at least 1.
   * Positive effects will generally cost more than negative ones.
   */
  public abstract int cost();
}
