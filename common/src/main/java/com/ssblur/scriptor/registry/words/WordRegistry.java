package com.ssblur.scriptor.registry.words;

import com.google.common.collect.HashBiMap;
import com.ssblur.scriptor.api.word.Action;
import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.api.word.Subject;
import com.ssblur.scriptor.api.word.Word;

public class WordRegistry {
  public static final WordRegistry INSTANCE = new WordRegistry();

  public static final Actions ACTIONS = new Actions();
  public static final PotionActions POTION_ACTIONS = new PotionActions();
  public static final ColorDescriptors COLOR_DESCRIPTORS = new ColorDescriptors();
  public static final Descriptors DESCRIPTORS = new Descriptors();
  public static final InventoryDescriptors INVENTORY_DESCRIPTORS = new InventoryDescriptors();
  public static final Subjects SUBJECTS = new Subjects();

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
