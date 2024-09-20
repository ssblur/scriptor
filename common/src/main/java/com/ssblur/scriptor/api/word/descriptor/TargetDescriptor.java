package com.ssblur.scriptor.api.word.descriptor;

import com.ssblur.scriptor.helpers.targetable.Targetable;

import java.util.List;

public interface TargetDescriptor {
  /**
   * Called to modify a spell's targets.
   * @param targetables The list of targets of this spell
   * @param owner The caster of this spell
   * @return A modified list of targets
   */
  List<Targetable> modifyTargets(List<Targetable> targetables, Targetable owner);

  /**
   * Whether to replace the cost of this spell's subject wholly.
   * @return true to replace subject cost
   */
  boolean replacesSubjectCost();
}
