package com.ssblur.scriptor.word;

import com.ssblur.scriptor.word.action.Action;
import com.ssblur.scriptor.word.descriptor.Descriptor;

import java.util.ArrayList;

public record PartialSpell(
  Action action,
  Descriptor... descriptors
) {
  public Descriptor[] deduplicatedDescriptors() {
    ArrayList<Descriptor> out = new ArrayList<>();
    for(var descriptor: descriptors) {
      if(descriptor.allowsDuplicates() || !out.contains(descriptor))
        out.add(descriptor);
    }
    return out.toArray(Descriptor[]::new);
  }
}
