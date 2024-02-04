package com.ssblur.scriptor.api;

import com.ssblur.scriptor.registry.words.WordRegistry;
import com.ssblur.scriptor.api.word.Word;
import com.ssblur.scriptor.api.word.Action;
import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.api.word.Subject;
import net.minecraft.resources.ResourceLocation;

public class ScriptorRegistry {
  /**
   * Registers a Word to Scriptor's WordRegistry, adding it to dictionaries.
   * @param key The key to register this word to.
   *            Will overwrite any word of the same type already written to this key.
   * @param word The word to register.
   *             Supports Actions, Subjects, and Descriptors.
   * @see Action
   * @see Subject
   * @see Descriptor
   * @return The word passed in.
   *         For chaining and static assignment.
   */
  public static Word register(String key, Word word) {
    if(word instanceof Action action) return WordRegistry.INSTANCE.register(key, action);
    if(word instanceof Subject subject) return WordRegistry.INSTANCE.register(key, subject);
    if(word instanceof Descriptor descriptor) return WordRegistry.INSTANCE.register(key, descriptor);
    throw new RuntimeException("Unknown Word type! Expected one of Action, Subject, or Descriptor.");
  }

  /**
   * Registers a Word to Scriptor's WordRegistry, adding it to dictionaries.
   * @param key The key to register this word to.
   *            Will overwrite any word of the same type already written to this key.
   * @param word The word to register.
   *             Supports Actions, Subjects, and Descriptors.
   * @see Action
   * @see Subject
   * @see Descriptor
   * @return The word passed in.
   *         For chaining and static assignment.
   */
  public static Word register(ResourceLocation key, Word word) {
    return register(key.toString(), word);
  }
}
