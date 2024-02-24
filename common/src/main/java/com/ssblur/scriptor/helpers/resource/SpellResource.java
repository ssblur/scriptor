package com.ssblur.scriptor.helpers.resource;

import java.util.List;

public class SpellResource {
  public static class PartialSpellResource {
    public String action;
    public List<String> descriptors;
    public PartialSpellResource(String action, List<String> descriptors) {
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
