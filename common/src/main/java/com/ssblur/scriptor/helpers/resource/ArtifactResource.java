package com.ssblur.scriptor.helpers.resource;

public class ArtifactResource {

  String name;
  boolean disabled = false;
  SpellResource spell;

  public boolean isDisabled() {
    return disabled;
  }


  public String getName() {
    return name;
  }

  public SpellResource getSpell() {
    return spell;
  }
}
