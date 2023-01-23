package com.ssblur.scriptor.word.subject;

import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.world.entity.Entity;

public class SelfSubject extends Subject{
  @Override
  public void cast(Entity caster, Spell spell) {
    spell.action().apply(caster, new EntityTargetable(caster), spell.descriptors());
  }

  @Override
  public Cost cost() { return new Cost(1, COSTTYPE.ADDITIVE); }
}
