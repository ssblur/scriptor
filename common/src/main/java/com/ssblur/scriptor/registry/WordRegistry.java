package com.ssblur.scriptor.registry;

import com.ssblur.scriptor.word.action.Action;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.subject.Subject;

import java.util.HashMap;

public class WordRegistry {
  public static HashMap<String, Action> actionRegistry = new HashMap<String, Action>();
  public static HashMap<String, Descriptor> descriptorRegistry = new HashMap<String, Descriptor>();
  public static HashMap<String, Subject> subjectRegistry = new HashMap<String, Subject>();

  /**
   * Register a new Action.
   * @param key The key at which to register this word
   * @param action The word to register
   */
  public static void register(String key, Action action) {
    actionRegistry.put(key, action);
  }

  /**
   * Register a new Descriptor.
   * @param key The key at which to register this word
   * @param descriptor The word to register
   */
  public static void register(String key, Descriptor descriptor) {
    descriptorRegistry.put(key, descriptor);
  }

  /**
   * Register a new Subject.
   * @param key The key at which to register this word
   * @param subject The word to register
   */
  public static void register(String key, Subject subject) {
    subjectRegistry.put(key, subject);
  }
}
