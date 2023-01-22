package com.ssblur.scriptor.word.subject;

import com.ssblur.scriptor.word.Spell;
import com.ssblur.scriptor.word.Word;
import net.minecraft.world.entity.Entity;

public abstract class Subject extends Word {
  /**
   * Called to cast this subject.
   * @param caster The Entity which cast this spell.
   * @param spell The Spell which is being cast.
   */
  public abstract void cast(Entity caster, Spell spell);

}
