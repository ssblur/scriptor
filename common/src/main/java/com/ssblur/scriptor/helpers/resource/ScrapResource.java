package com.ssblur.scriptor.helpers.resource;

import com.ssblur.scriptor.error.WordNotFoundException;
import com.ssblur.scriptor.registry.words.WordRegistry;
import com.ssblur.scriptor.word.PartialSpell;
import com.ssblur.scriptor.word.Spell;
import com.ssblur.scriptor.word.action.Action;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.subject.Subject;

import java.util.ArrayList;
import java.util.List;

public class ScrapResource {
  String[] keys;
  boolean disabled = false;
  int tier;

  public boolean isDisabled() {
    return disabled;
  }

  public int getTier() { return tier; }

  public String[] getKeys() { return keys; }
}
