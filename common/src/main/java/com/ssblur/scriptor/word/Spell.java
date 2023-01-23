package com.ssblur.scriptor.word;

import com.ssblur.scriptor.word.action.Action;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.subject.Subject;
import net.minecraft.world.entity.Entity;

/**
 * A record used to represent a complete spell.
 * @param action The active component of a spell
 * @param subject The target of a spell
 * @param descriptors Any descriptors affecting this spell
 */
public record Spell(
  Action action,
  Subject subject,
  Descriptor... descriptors
) {
  /**
   * Casts this spell.
   * @param caster The entity which cast this spell.
   */
  public void cast(Entity caster) {
    subject.cast(caster, this);
  }

  /**
   * The cost for this spell, generally affects cooldowns / material cost.
   * @return A number representing cost.
   */
  public double cost() {
    double sum = 0;
    double scalar = 1;
    for(var d: descriptors) {
      var cost = d.cost();
      switch (cost.type()){
        case ADDITIVE -> sum += cost.cost();
        case MULTIPLICATIVE -> scalar += cost.cost();
      }
    }

    var cost = action.cost();
    switch (cost.type()){
      case ADDITIVE -> sum += cost.cost();
      case MULTIPLICATIVE -> scalar += cost.cost();
    }

    cost = subject.cost();
    switch (cost.type()){
      case ADDITIVE -> sum += cost.cost();
      case MULTIPLICATIVE -> scalar += cost.cost();
    }

    return sum * scalar;
  }
}
