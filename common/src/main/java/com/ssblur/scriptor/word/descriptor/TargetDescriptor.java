package com.ssblur.scriptor.word.descriptor;

import com.ssblur.scriptor.helpers.targetable.Targetable;

import java.util.List;

public interface TargetDescriptor {
  List<Targetable> modifyTargets(List<Targetable> targetables);
  boolean replacesSubjectCost();
}
