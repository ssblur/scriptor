package com.ssblur.scriptor.word.descriptor.target;

import com.ssblur.scriptor.helpers.targetable.Targetable;

import java.util.List;

public interface TargetDescriptor {
  List<Targetable> modifyTargets(List<Targetable> targetables, Targetable owner);
  boolean replacesSubjectCost();
}
