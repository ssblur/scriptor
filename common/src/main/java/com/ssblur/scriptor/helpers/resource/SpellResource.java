package com.ssblur.scriptor.helpers.resource;

import com.google.gson.JsonPrimitive;

import java.util.List;

public class SpellResource {
  public static class PartialSpellResource {
    public String action;
    public List<JsonPrimitive> descriptors;

    public PartialSpellResource(String action, List<JsonPrimitive> descriptors) {
      this.action = action;
      this.descriptors = descriptors;
    }
  }

  public String subject;
  public List<PartialSpellResource> spells;
  public SpellResource(String subject, List<PartialSpellResource> spells) {
    this.subject = subject;
    this.spells = spells;
  }
}
