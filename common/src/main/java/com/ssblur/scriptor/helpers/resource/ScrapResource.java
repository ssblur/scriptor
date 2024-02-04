package com.ssblur.scriptor.helpers.resource;

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
