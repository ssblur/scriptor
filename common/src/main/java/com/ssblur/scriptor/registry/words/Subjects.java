package com.ssblur.scriptor.registry.words;

import com.ssblur.scriptor.word.subject.*;

public class Subjects {
  private static final WordRegistry INSTANCE = WordRegistry.INSTANCE;

  public final Subject SELF = INSTANCE.register("self", new SelfSubject());
  public final Subject TOUCH = INSTANCE.register("touch", new TouchSubject());
  public final Subject HITSCAN = INSTANCE.register("hitscan", new HitscanSubject());
  public final Subject PROJECTILE = INSTANCE.register("projectile", new ProjectileSubject());
  public final Subject STORM = INSTANCE.register("storm", new StormSubject());
  public final Subject RUNE = INSTANCE.register("rune", new RuneSubject());
  public final Subject ENCHANT = INSTANCE.register("enchant", new ImbueSubject());
}
