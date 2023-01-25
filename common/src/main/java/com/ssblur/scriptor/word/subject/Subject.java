package com.ssblur.scriptor.word.subject;

import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.Spell;
import com.ssblur.scriptor.word.Word;
import net.minecraft.world.entity.Entity;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class Subject extends Word {
  /**
   * Called to get targets for this Subject.
   * Actions will not be applied until resolved.
   * @param caster The Entity which cast this spell.
   * @param spell The Spell which is being cast.
   */
  public abstract CompletableFuture<List<Targetable>> getTargets(Entity caster, Spell spell);
}
