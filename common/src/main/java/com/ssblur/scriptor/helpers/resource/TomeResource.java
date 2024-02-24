package com.ssblur.scriptor.helpers.resource;

public class TomeResource {

  String name;
  String author;
  SpellResource spell;
  String item;
  int tier;

  public String getAuthor() {
    return author;
  }

  public String getName() {
    return name;
  }
  public int getTier() { return tier; }

  public String getItem() { return item; }

  public SpellResource getSpell() {
    return spell;
  }
}
