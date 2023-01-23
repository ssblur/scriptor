package com.ssblur.scriptor.word.subject;

import com.ssblur.scriptor.word.Spell;
import net.minecraft.world.entity.Entity;

public class DebugSubject extends Subject{
  @Override
  public void cast(Entity caster, Spell spell) {}

  @Override
  public int cost() { return 1; }
}
