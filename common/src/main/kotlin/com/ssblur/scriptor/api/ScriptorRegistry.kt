package com.ssblur.scriptor.api

import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.api.word.Subject
import com.ssblur.scriptor.api.word.Word
import com.ssblur.scriptor.registry.words.WordRegistry
import net.minecraft.resources.ResourceLocation

@Suppress("unused")
object ScriptorRegistry {
    /**
     * Registers a Word to Scriptor's WordRegistry, adding it to dictionaries.
     * @param key The key to register this word to.
     * Will overwrite any word of the same type already written to this key.
     * @param word The word to register.
     * Supports Actions, Subjects, and Descriptors.
     * @see Action
     * @see Subject
     * @see Descriptor
     * @return The word passed in.
     * For chaining and static assignment.
     */
    fun register(key: String, word: Word): Word {
        if (word is Action) return WordRegistry.register(key, word)!!
        if (word is Subject) return WordRegistry.register(key, word)!!
        if (word is Descriptor) return WordRegistry.register(key, word)!!
        throw RuntimeException("Unknown Word type! Expected one of Action, Subject, or Descriptor.")
    }

    /**
     * Registers a Word to Scriptor's WordRegistry, adding it to dictionaries.
     * @param key The key to register this word to.
     * Will overwrite any word of the same type already written to this key.
     * @param word The word to register.
     * Supports Actions, Subjects, and Descriptors.
     * @see Action
     * @see Subject
     * @see Descriptor
     * @return The word passed in.
     * For chaining and static assignment.
     */
    fun register(key: ResourceLocation, word: Word): Word {
        return register(key.toString(), word)
    }
}
