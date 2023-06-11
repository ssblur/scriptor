package com.ssblur.scriptor.registry;

import com.google.common.collect.HashBiMap;
import com.ssblur.scriptor.word.Word;
import com.ssblur.scriptor.word.action.*;
import com.ssblur.scriptor.word.action.potion.PoisonAction;
import com.ssblur.scriptor.word.action.potion.SlowAction;
import com.ssblur.scriptor.word.action.teleport.BringAction;
import com.ssblur.scriptor.word.action.teleport.GotoAction;
import com.ssblur.scriptor.word.action.teleport.SwapAction;
import com.ssblur.scriptor.word.descriptor.*;
import com.ssblur.scriptor.word.descriptor.discount.BloodCostDescriptor;
import com.ssblur.scriptor.word.descriptor.discount.CheapDescriptor;
import com.ssblur.scriptor.word.descriptor.discount.HealthyDescriptor;
import com.ssblur.scriptor.word.descriptor.discount.PoisonDescriptor;
import com.ssblur.scriptor.word.descriptor.duration.SimpleDurationDescriptor;
import com.ssblur.scriptor.word.descriptor.focus.inventory.CasterFirstEmptySlotDescriptor;
import com.ssblur.scriptor.word.descriptor.focus.inventory.CasterFirstFilledSlotDescriptor;
import com.ssblur.scriptor.word.descriptor.focus.inventory.CasterIgnoreTargetedSlotDescriptor;
import com.ssblur.scriptor.word.descriptor.focus.inventory.CasterInventoryDescriptor;
import com.ssblur.scriptor.word.descriptor.power.BloodPowerDescriptor;
import com.ssblur.scriptor.word.descriptor.power.OverwhelmingStrengthDescriptor;
import com.ssblur.scriptor.word.descriptor.power.SimpleStrengthDescriptor;
import com.ssblur.scriptor.word.descriptor.target.ChainDescriptor;
import com.ssblur.scriptor.word.descriptor.target.inventory.FirstEmptySlotDescriptor;
import com.ssblur.scriptor.word.descriptor.target.inventory.FirstFilledSlotDescriptor;
import com.ssblur.scriptor.word.descriptor.target.inventory.IgnoreTargetedSlotDescriptor;
import com.ssblur.scriptor.word.descriptor.target.inventory.InventoryDescriptor;
import com.ssblur.scriptor.word.descriptor.visual.ColorDescriptor;
import com.ssblur.scriptor.word.subject.*;

import java.util.HashMap;

public class WordRegistry {
  public static final WordRegistry INSTANCE = new WordRegistry();

  public static final Subject SELF = INSTANCE.register("self", new SelfSubject());
  public static final Subject TOUCH = INSTANCE.register("touch", new TouchSubject());
  public static final Subject PROJECTILE = INSTANCE.register("projectile", new ProjectileSubject());
  public static final Subject STORM = INSTANCE.register("storm", new StormSubject());
  public static final Subject RUNE = INSTANCE.register("rune", new RuneSubject());
  public static final Subject ENCHANT = INSTANCE.register("enchant", new ImbueSubject());

  public static final Descriptor LONG = INSTANCE.register("long", new SimpleDurationDescriptor(3, 7));
  public static final Descriptor SLOW = INSTANCE.register("slow", new SpeedDurationDescriptor(2, 4, .75));
  public static final Descriptor FAST = INSTANCE.register("fast", new SpeedDurationDescriptor(2, -4, 1.25));
  public static final Descriptor CHAIN = INSTANCE.register("chain", new ChainDescriptor());
  public static final Descriptor POISONED = INSTANCE.register("poisoned", new PoisonDescriptor());

  public static final Descriptor BLOOD_POWER = INSTANCE.register("blood_power", new BloodPowerDescriptor());
  public static final Descriptor STRONG = INSTANCE.register("strong", new SimpleStrengthDescriptor(2, 1));
  public static final Descriptor POWERFUL = INSTANCE.register("powerful", new SimpleStrengthDescriptor(6, 4));
  public static final Descriptor STACKING_STRONG = INSTANCE.register("stacking_strong", new SimpleStrengthDescriptor(4, 1).allowDuplication());
  public static final Descriptor OVERWHELMING_STRENGTH = INSTANCE.register("overwhelming", new OverwhelmingStrengthDescriptor());

  public static final Descriptor BLOOD_COST = INSTANCE.register("blood_cost", new BloodCostDescriptor());
  public static final Descriptor CHEAP = INSTANCE.register("cheap", new CheapDescriptor());
  public static final Descriptor HEALTHY = INSTANCE.register("healthy", new HealthyDescriptor());

  public static final Descriptor WHITE = INSTANCE.register("white", new ColorDescriptor(0xe4e4e4));
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
  public static final Descriptor RAINBOW = INSTANCE.register("rainbow", new ColorDescriptor(-1));

  public static final Descriptor INVENTORY = INSTANCE.register("inventory", new InventoryDescriptor());
  public static final Descriptor FIRST_EMPTY = INSTANCE.register("first_empty", new FirstEmptySlotDescriptor());
  public static final Descriptor FIRST_FILLED = INSTANCE.register("first_filled", new FirstFilledSlotDescriptor());
  public static final Descriptor FIRST_MATCHING = INSTANCE.register("first_matching", new IgnoreTargetedSlotDescriptor());

  public static final Descriptor CASTER_INVENTORY = INSTANCE.register("caster_inventory", new CasterInventoryDescriptor());
  public static final Descriptor CASTER_FIRST_EMPTY = INSTANCE.register("caster_first_empty", new CasterFirstEmptySlotDescriptor());
  public static final Descriptor CASTER_FIRST_FILLED = INSTANCE.register("caster_first_filled", new CasterFirstFilledSlotDescriptor());
  public static final Descriptor CASTER_FIRST_MATCHING = INSTANCE.register("caster_first_matching", new CasterIgnoreTargetedSlotDescriptor());

  public static final Action INFLAME = INSTANCE.register("inflame", new InflameAction());
  public static final Action LIGHT = INSTANCE.register("light", new LightAction());
  public static final Action HEAL = INSTANCE.register("heal", new HealAction());
  public static final Action SMITE = INSTANCE.register("smite", new SmiteAction());
  public static final Action EXPLOSION = INSTANCE.register("explosion", new ExplosionAction());
  public static final Action GOTO = INSTANCE.register("goto", new GotoAction());
  public static final Action SWAP = INSTANCE.register("swap", new SwapAction());
  public static final Action BRING = INSTANCE.register("bring", new BringAction());
  public static final Action BREAK = INSTANCE.register("break", new BreakBlockAction());
  public static final Action HARM = INSTANCE.register("harm", new HarmAction());
  public static final Action POISON_POTION = INSTANCE.register("poison", new PoisonAction());
  public static final Action SLOW_POTION = INSTANCE.register("slow", new SlowAction());

  public HashBiMap<String, Action> actionRegistry = HashBiMap.create();
  public HashBiMap<String, Descriptor> descriptorRegistry = HashBiMap.create();
  public HashBiMap<String, Subject> subjectRegistry = HashBiMap.create();

  /**
   * Tries to get find a word's key in the relevant registry.
   * @param word The word to search for
   * @return The associated key
   */
  public String getKey(Word word) {
    if(word instanceof Action action)
      return actionRegistry.inverse().get(action);
    else if (word instanceof Descriptor descriptor)
      return descriptorRegistry.inverse().get(descriptor);
    else if (word instanceof Subject subject)
      return subjectRegistry.inverse().get(subject);
    return null;
  }

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
