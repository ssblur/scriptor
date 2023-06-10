package com.ssblur.scriptor.helpers;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ssblur.scriptor.helpers.language.DefaultTokenGenerators;
import com.ssblur.scriptor.helpers.language.TokenGenerator;
import com.ssblur.scriptor.registry.WordRegistry;
import com.ssblur.scriptor.word.Spell;
import com.ssblur.scriptor.word.action.Action;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.subject.Subject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import com.ssblur.scriptor.ScriptorMod;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DictionarySavedData extends SavedData {
  public List<WORD> spellStructure;
  public BiMap<String, String> words;

  /**
   * A class for storing language data for a given world.
   * @param spellStructure The structure of spells for this world.
   * @param words All words which exist in this world's lexicon.
   */
  public DictionarySavedData(
    List<String> spellStructure,
    List<Pair<String, String>> words
  ) {
    this.spellStructure = spellStructure.stream().map(WORD::valueOf).toList();
    this.words = HashBiMap.create();
    for(var pair: words)
      this.words.put(pair.getFirst(), pair.getSecond());

    generateMissingWords();
    setDirty();
  }

  /**
   * An enum representing types of words used in Spells.
   */
  public enum WORD {
    ACTION,
    DESCRIPTOR,
    SUBJECT
  }


  public static final Codec<DictionarySavedData> worldCodec = RecordCodecBuilder.create(instance -> instance.group(
    Codec.STRING.listOf().fieldOf("spellStructure").forGetter(worldData -> worldData.spellStructure.stream().map(WORD::toString).toList()),
    Codec.compoundList(Codec.STRING, Codec.STRING)
      .fieldOf("words")
      .forGetter(
        worldData ->
          worldData.words.keySet().stream().map(key -> new Pair<>(key, worldData.words.get(key))).toList()
      )
  ).apply(instance, DictionarySavedData::new));

  boolean containsKey(String key) {
    return words.containsKey(key);
  }

  boolean containsWord(String string) {
    return words.containsValue(string);
  }

  void generateMissingWords() {
    TokenGenerator generator = DefaultTokenGenerators.angGenerator;
    String token;
    Random random = new Random();

    for(var word: WordRegistry.INSTANCE.actionRegistry.keySet()) {
      if(containsKey("action:" + word))
        continue;

      do {
        token = generator.generateToken();
      } while (containsWord(token));

      words.put("action:" + word, token);
    }

    for(var word: WordRegistry.INSTANCE.descriptorRegistry.keySet()) {
      if(containsKey("descriptor:" + word))
        continue;

      do {
        token = generator.generateToken();
      } while (containsWord(token));

      words.put("descriptor:" + word, token);
    }
    for(var word: WordRegistry.INSTANCE.subjectRegistry.keySet()) {
      if(containsKey("subject:" + word))
        continue;

      do {
        token = generator.generateToken();
      } while (containsWord(token));

      words.put("subject:" + word, token);

    }
  }

  public DictionarySavedData() {
    WORD[] basicStructure = new WORD[]{WORD.ACTION, WORD.DESCRIPTOR, WORD.SUBJECT};
    List<WORD> structure = Arrays.asList(basicStructure);
    Collections.shuffle(structure);

    String token;
    Random random = new Random();
    TokenGenerator shortGenerator = DefaultTokenGenerators.shortAngGenerator;

    spellStructure = new ArrayList<>();
    spellStructure.addAll(structure);

    words = HashBiMap.create();
    generateMissingWords();

    setDirty();
  }

  /**
   * A helper for parsing a written word into corresponding WordData.
   * @param word The written word to parse
   * @return The matching word key
   * null if no matches
   */
  public String parseWord(String word) {
    return words.inverse().get(word);
  }

  /**
   * A helper for getting WordData based on its key.
   * @param key The key used to register a Word
   * @return The matching word
   * null if no matches
   */
  public String getWord(String key) {
    return words.get(key);
  }

  /**
   * A helper for getting a key based on a Word.
   * @param action The Action to search by
   * @return The matching key
   * null if no matches
   */
  public String getKey(Action action) {
    for(String key: WordRegistry.INSTANCE.actionRegistry.keySet()) {
      if(action == WordRegistry.INSTANCE.actionRegistry.get(key))
        return "action:" + key;
    }
    return null;
  }

  /**
   * A helper for getting a key based on a Word.
   * @param descriptor The Descriptor to search by
   * @return The matching key
   * null if no matches
   */
  public String getKey(Descriptor descriptor) {
    for(String key: WordRegistry.INSTANCE.descriptorRegistry.keySet()) {
      if(descriptor == WordRegistry.INSTANCE.descriptorRegistry.get(key))
        return "descriptor:" + key;
    }
    return null;
  }

  /**
   * A helper for getting a key based on a Word.
   * @param subject The Subject to search by
   * @return The matching key
   * null if no matches
   */
  public String getKey(Subject subject) {
    for(String key: WordRegistry.INSTANCE.subjectRegistry.keySet()) {
      if(subject == WordRegistry.INSTANCE.subjectRegistry.get(key))
        return "subject:" + key;
    }
    return null;
  }


  /**
   * A helper for getting WordData based on a Word.
   * @param action The Action to search by
   * @return The matching word
   * null if no matches
   */
  public String getWord(Action action) {
    for(String key: WordRegistry.INSTANCE.actionRegistry.keySet()) {
      if(action == WordRegistry.INSTANCE.actionRegistry.get(key))
        return getWord("action:" + key);
    }
    return null;
  }

  /**
   * A helper for getting WordData based on a Word.
   * @param descriptor The Descriptor to search by
   * @return The matching word
   * null if no matches
   */
  public String getWord(Descriptor descriptor) {
    for(String key: WordRegistry.INSTANCE.descriptorRegistry.keySet()) {
      if(descriptor == WordRegistry.INSTANCE.descriptorRegistry.get(key))
        return getWord("descriptor:" + key);
    }
    return null;
  }

  /**
   * A helper for getting WordData based on a Word.
   * @param subject The Subject to search by
   * @return The matching word
   * null if no matches
   */
  public String getWord(Subject subject) {
    for(String key: WordRegistry.INSTANCE.subjectRegistry.keySet()) {
      if(subject == WordRegistry.INSTANCE.subjectRegistry.get(key))
        return getWord("subject:" + key);
    }
    return null;
  }


  /**
   * Attempt to parse a String into a Spell
   * @param text The String to parse
   * @return The associated Spell
   * null if invalid
   */
  @Nullable
  public Spell parse(@Nullable String text) {
    if(text == null) {
      ScriptorMod.LOGGER.warn("No text provided to parser! This shouldn't happen.");
      return null;
    }
    int position = 0;
    int tokenPosition = 0;
    try {
      String[] tokens = text.split("\s");

      Action action = null;
      List<Descriptor> descriptors = new ArrayList<>();
      Subject subject = null;

      while (position < spellStructure.size() && tokenPosition < tokens.length) {
        WORD word = spellStructure.get(position);
        String wordData;
        switch (word) {
          case ACTION -> {
            wordData = parseWord(tokens[tokenPosition]);
            if (wordData == null) {
              ScriptorMod.LOGGER.debug("Failed to process spell with text: \"" + text + "\"");
              ScriptorMod.LOGGER.debug("No word found for \"" + tokens[tokenPosition] + "\", action expected");
              return null;
            }
            action = WordRegistry.INSTANCE.actionRegistry.get(wordData.substring(7));
          }
          case DESCRIPTOR -> {
            wordData = parseWord(tokens[tokenPosition]);
            // Descriptors aren't required. If there are none, roll forward as necessary and continue.
            if (wordData == null) {
              position++;
              continue;
            }
            Descriptor descriptor = WordRegistry.INSTANCE.descriptorRegistry.get(wordData.substring(11));
            if (descriptor == null) {
              position++;
              continue;
            }
            descriptors.add(descriptor);

            // If there are enough tokens to have more descriptors, process descriptors again.
            if ((tokens.length - tokenPosition) > (spellStructure.size() - position)) {
              position++;
              tokenPosition++;
              position -= 2;
            }
          }
          case SUBJECT -> {
            wordData = parseWord(tokens[tokenPosition]);
            if (wordData == null) {
              ScriptorMod.LOGGER.debug("Failed to process spell with text: \"" + text + "\"");
              ScriptorMod.LOGGER.debug("Subject " + tokens[tokenPosition] + " not found");
              return null;
            }
            subject = WordRegistry.INSTANCE.subjectRegistry.get(wordData.substring(8));
          }
        }

        position++;
        tokenPosition++;
      }

      if (action != null && subject != null)
        return new Spell(action, subject, descriptors.toArray(Descriptor[]::new));
    } catch (Exception e) {
      ScriptorMod.LOGGER.warn("==========================================================");
      ScriptorMod.LOGGER.warn("The below error did NOT cause a crash, this is debug info!");
      ScriptorMod.LOGGER.error("Error:", e);
      ScriptorMod.LOGGER.warn("==========================================================");
    }
    ScriptorMod.LOGGER.debug("Failed to process spell with text: \"" + text + "\"");
    return null;
  }

  /**
   * Attempt to parse a String into lists of components
   * @param text The String to parse
   * @return The associated Spell
   * null if invalid
   */
  public List<String> parseComponents(String text) {
    var spell = parse(text);
    if(spell != null)
      return Arrays.asList(generate(spell).split("\\s"));
    return null;
  }


  /**
   * A helper for generating a String to describe a spell.
   * @param spell The Spell to generate text for.
   * @return A String to describe a spell
   */
  public String generate(Spell spell) {
    StringBuilder descriptorBuilder = new StringBuilder();
    for(Descriptor descriptor: spell.deduplicatedDescriptors()) {
      descriptorBuilder.append(" ").append(getWord(descriptor));
    }

    StringBuilder builder = new StringBuilder();
    for(WORD w: spellStructure) {
      if(w == WORD.ACTION)
        builder.append(" ").append(getWord(spell.action()));
      else if(w == WORD.SUBJECT)
        builder.append(" ").append(getWord(spell.subject()));
      else if(w == WORD.DESCRIPTOR)
        builder.append(descriptorBuilder);
    }

    return builder.toString().strip();
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    var result = worldCodec.encodeStart(NbtOps.INSTANCE, this).get().left();
    result.ifPresent(value -> tag.put("scriptor:dictionary", value));
    return tag;
  }

  public static DictionarySavedData load(CompoundTag tag) {
    var input = tag.get("scriptor:dictionary");
    if(input != null) {
      var result = worldCodec.decode(NbtOps.INSTANCE, input).get().left();
      if(result.isPresent() && result.get().getFirst() != null)
        return result.get().getFirst();
    }
    return null;
  }

  /**
   * A helper for calling computeIfAbsent for this class from the Overworld
   * @param level Any ServerLevel
   * @return The DictionarySavedData for this world
   */
  public static DictionarySavedData computeIfAbsent(ServerLevel level) {
    ServerLevel server = level.getServer().getLevel(Level.OVERWORLD);
    Objects.requireNonNull(server);
    return server.getDataStorage().computeIfAbsent(DictionarySavedData::load, DictionarySavedData::new, "scriptor_dictionary");
  }

  @Override
  public String toString() {
    var builder = new StringBuilder();

    builder.append("\n\nStructure:\n");
    for(var w: spellStructure)
      builder.append(w).append(" ");
    builder.append("\n\n");

    builder.append("Words:\n");
    for(var k: words.keySet()) {
      builder.append('"').append(k).append('"');
      builder.append(" : ");
      builder.append(words.get(k));
      builder.append("\n");
    }

    return builder.toString();
  }
}
