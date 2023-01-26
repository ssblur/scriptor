package com.ssblur.scriptor.registry;

import com.ssblur.scriptor.word.action.Action;
import com.ssblur.scriptor.word.action.HealAction;
import com.ssblur.scriptor.word.action.InflameAction;
import com.ssblur.scriptor.word.action.SmiteAction;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.SimpleDurationDescriptor;
import com.ssblur.scriptor.word.descriptor.SimpleStrengthDescriptor;
import com.ssblur.scriptor.word.subject.SelfSubject;
import com.ssblur.scriptor.word.subject.StormSubject;
import com.ssblur.scriptor.word.subject.Subject;
import com.ssblur.scriptor.word.subject.TouchSubject;

import java.util.HashMap;

public class WordRegistry {
  public static final WordRegistry INSTANCE = new WordRegistry();

  public static final Subject SELF = INSTANCE.register("self", new SelfSubject());
  public static final Subject TOUCH = INSTANCE.register("touch", new TouchSubject());
  public static final Subject STORM = INSTANCE.register("storm", new StormSubject());

  public static final Descriptor LONG = INSTANCE.register("long", new SimpleDurationDescriptor(3, 7));
  public static final Descriptor SLOW = INSTANCE.register("slow", new SimpleDurationDescriptor(2, 4));
  public static final Descriptor STRONG = INSTANCE.register("strong", new SimpleStrengthDescriptor(2, 1));

  public static final Action INFLAME = INSTANCE.register("inflame", new InflameAction());
  public static final Action HEAL = INSTANCE.register("heal", new HealAction());
  public static final Action SMITE = INSTANCE.register("smite", new SmiteAction());

  public HashMap<String, Action> actionRegistry = new HashMap<>();
  public HashMap<String, Descriptor> descriptorRegistry = new HashMap<>();
  public HashMap<String, Subject> subjectRegistry = new HashMap<>();

  /**
   * Register a new Action.
   * @param key The key at which to register this word
   * @param action The word to register
   */
  public Action register(String key, Action action) {
    return actionRegistry.put(key, action);
  }

  /**
   * Register a new Descriptor.
   * @param key The key at which to register this word
   * @param descriptor The word to register
   */
  public Descriptor register(String key, Descriptor descriptor) {
    return descriptorRegistry.put(key, descriptor);
  }

  /**
   * Register a new Subject.
   * @param key The key at which to register this word
   * @param subject The word to register
   */
  public Subject register(String key, Subject subject) {
    return subjectRegistry.put(key, subject);
  }
}
