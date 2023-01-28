package com.ssblur.scriptor.registry;

import com.ssblur.scriptor.word.action.Action;
import com.ssblur.scriptor.word.action.HealAction;
import com.ssblur.scriptor.word.action.InflameAction;
import com.ssblur.scriptor.word.action.SmiteAction;
import com.ssblur.scriptor.word.descriptor.*;
import com.ssblur.scriptor.word.subject.*;

import java.util.HashMap;

public class WordRegistry {
  public static final WordRegistry INSTANCE = new WordRegistry();

  public static final Subject SELF = INSTANCE.register("self", new SelfSubject());
  public static final Subject TOUCH = INSTANCE.register("touch", new TouchSubject());
  public static final Subject PROJECTILE = INSTANCE.register("projectile", new ProjectileSubject());
  public static final Subject STORM = INSTANCE.register("storm", new StormSubject());

  public static final Descriptor LONG = INSTANCE.register("long", new SimpleDurationDescriptor(3, 7));
  public static final Descriptor SLOW = INSTANCE.register("slow", new SpeedDurationDescriptor(2, 4, .75));
  public static final Descriptor FAST = INSTANCE.register("fast", new SpeedDurationDescriptor(2, -4, 1.25));
  public static final Descriptor STRONG = INSTANCE.register("strong", new SimpleStrengthDescriptor(2, 1));

  public static final Descriptor WHITE = INSTANCE.register("black", new ColorDescriptor(0xe4e4e4));
  public static final Descriptor LIGHT_GRAY = INSTANCE.register("light_gray", new ColorDescriptor(0xa0a7a7));
  public static final Descriptor DARK_GRAY = INSTANCE.register("dark_gray", new ColorDescriptor(0x414141));
  public static final Descriptor BLACK = INSTANCE.register("black", new ColorDescriptor(0x181414));
  public static final Descriptor RED = INSTANCE.register("red", new ColorDescriptor(0x9e2b27));
  public static final Descriptor ORANGE = INSTANCE.register("orange", new ColorDescriptor(0xea7e35));
  public static final Descriptor YELLOW = INSTANCE.register("yellow", new ColorDescriptor(0xc2b51c));
  public static final Descriptor LIME_GREEN = INSTANCE.register("lime_green", new ColorDescriptor(0x39ba2e));
  public static final Descriptor GREEN = INSTANCE.register("green", new ColorDescriptor(0x364b18));
  public static final Descriptor LIGHT_BLUE = INSTANCE.register("light_blue", new ColorDescriptor(0x6387d2));
  public static final Descriptor CYAN = INSTANCE.register("cyan", new ColorDescriptor(0x267191));
  public static final Descriptor BLUE = INSTANCE.register("blue", new ColorDescriptor(0x253193));
  public static final Descriptor PURPLE = INSTANCE.register("purple", new ColorDescriptor(0x7e34bf));
  public static final Descriptor MAGENTA = INSTANCE.register("magenta", new ColorDescriptor(0xbe49c9));
  public static final Descriptor PINK = INSTANCE.register("pink", new ColorDescriptor(0xd98199));
  public static final Descriptor BROWN = INSTANCE.register("brown", new ColorDescriptor(0x56331c));

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
