package com.ssblur.scriptor.helpers;

import com.ssblur.scriptor.error.WordNotFoundException;
import com.ssblur.scriptor.registry.WordRegistry;
import com.ssblur.scriptor.word.PartialSpell;
import com.ssblur.scriptor.word.Spell;
import com.ssblur.scriptor.word.action.Action;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.subject.Subject;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class TomeResource {
  public static class SpellResource {
    String action;
    String subject;
    List<String> descriptors;
    public SpellResource(String action, String subject, List<String> descriptors) {
      this.action = action;
      this.subject = subject;
      this.descriptors = descriptors;
    }
  }

  String name;
  boolean disabled = false;
  String author;
  SpellResource spell;
  int tier;

  public boolean isDisabled() {
    return disabled;
  }

  public String getAuthor() {
    return author;
  }

  public String getName() {
    return name;
  }
  public int getTier() { return tier; }

  public Spell getSpell() {
    Action action = WordRegistry.INSTANCE.actionRegistry.get(spell.action);
    if(action == null)
      throw new WordNotFoundException(spell.action);

    Subject subject = WordRegistry.INSTANCE.subjectRegistry.get(spell.subject);
    if(subject == null)
      throw new WordNotFoundException(spell.subject);

    ArrayList<Descriptor> descriptors = new ArrayList<>();
    for(String string: spell.descriptors) {
      Descriptor descriptor = WordRegistry.INSTANCE.descriptorRegistry.get(string);
      if(descriptor == null)
        throw new WordNotFoundException(string);
      descriptors.add(descriptor);
    }

    return new Spell(subject, new PartialSpell(action, descriptors.toArray(Descriptor[]::new)));
  }
}
