package com.ssblur.scriptor.registry;

import com.ssblur.scriptor.word.action.Action;
import com.ssblur.scriptor.word.action.HealAction;
import com.ssblur.scriptor.word.action.InflameAction;
import com.ssblur.scriptor.word.action.SmiteAction;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.DurationDescriptor;
import com.ssblur.scriptor.word.descriptor.ExtensionDescriptor;
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

  public static final Descriptor LONG = INSTANCE.register("long", new DurationDescriptor());
  public static final Descriptor SLOW = INSTANCE.register("slow", new DurationDescriptor());
  public static final Descriptor STRONG = INSTANCE.register("strong", new ExtensionDescriptor());
//  public static final Descriptor WOOD = WORDS.register("wood", new ElementalDescriptor());
//  public static final Descriptor FIRE = WORDS.register("fire", new ElementalDescriptor());
//  public static final Descriptor EARTH = WORDS.register("earth", new ElementalDescriptor());
//  public static final Descriptor GOLD = WORDS.register("gold", new ElementalDescriptor());
//  public static final Descriptor WATER = WORDS.register("water", new ElementalDescriptor());

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
