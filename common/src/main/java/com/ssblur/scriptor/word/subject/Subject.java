package com.ssblur.scriptor.word.subject;

import com.ssblur.scriptor.word.Spell;
import net.minecraft.world.entity.Entity;

public abstract class Subject {
  /**
   * Called to cast this subject.
   * @param caster The Entity which cast this spell.
   * @param spell The Spell which is being cast.
   */
  public abstract void cast(Entity caster, Spell spell);

  /**
   * @return A number representing material cost or cast cooldown.
   * Generally, for a subject, the simplest / most accessible option should
   * cost 0, and more powerful or complex options should cost more.
   */
  public abstract int cost();
}
