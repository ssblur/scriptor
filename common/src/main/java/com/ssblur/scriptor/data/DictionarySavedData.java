package com.ssblur.scriptor.data;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.api.word.Action;
import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.api.word.Subject;
import com.ssblur.scriptor.api.word.Word;
import com.ssblur.scriptor.helpers.resource.SpellResource;
import com.ssblur.scriptor.registry.TokenGeneratorRegistry;
import com.ssblur.scriptor.registry.words.WordRegistry;
import com.ssblur.scriptor.word.PartialSpell;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DictionarySavedData extends SavedData {
  private static final Logger LOGGER = ScriptorMod.LOGGER;
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
    var registry = TokenGeneratorRegistry.INSTANCE;
    String token;

    for(var word: WordRegistry.INSTANCE.otherRegistry) {
      if(containsKey("other:" + word))
        continue;

      do {
        token = registry.generateWord("other:" + word);
      } while (containsWord(token));

      words.put("other:" + word, token);
    }

    for(var word: WordRegistry.INSTANCE.actionRegistry.keySet()) {
      if(containsKey("action:" + word))
        continue;

      do {
        token = registry.generateWord("action:" + word);
      } while (containsWord(token));

      words.put("action:" + word, token);
    }

    for(var word: WordRegistry.INSTANCE.descriptorRegistry.keySet()) {
      if(containsKey("descriptor:" + word))
        continue;

      do {
        token = registry.generateWord("descriptor:" + word);
      } while (containsWord(token));

      words.put("descriptor:" + word, token);
    }
    for(var word: WordRegistry.INSTANCE.subjectRegistry.keySet()) {
      if(containsKey("subject:" + word))
        continue;

      do {
        token = registry.generateWord("subject:" + word);
      } while (containsWord(token));

      words.put("subject:" + word, token);
    }
  }

  public DictionarySavedData() {
    WORD[] basicStructure = new WORD[]{WORD.SUBJECT, WORD.ACTION, WORD.DESCRIPTOR};
    List<WORD> structure = Arrays.asList(basicStructure);
    if(!ScriptorMod.COMMUNITY_MODE)
      Collections.shuffle(structure);

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
   * @param word The Word to search by
   * @return The matching key
   * null if no matches
   */
  public String getKey(Word word) {
    var key = WordRegistry.INSTANCE.getKey(word);
    if(key != null)
      if(word instanceof Action)
        return "action:" + key;
      else if(word instanceof Subject)
        return "subject:" + key;
      else if(word instanceof Descriptor)
        return "descriptor:" + key;
    return null;
  }


  /**
   * A helper for getting WordData based on a Word.
   * @param word The Word to search by
   * @return The matching word
   * null if no matches
   */
  public String getWord(Word word) {
    return getWord(getKey(word));
  }

  private @Nullable WORD lastWord(WORD word) {
    if(spellStructure.indexOf(word) == 0)
      return null;
    return spellStructure.get(spellStructure.indexOf(word) - 1);
  }

  private boolean checkLastWord(WORD word, @Nullable WORD prevWord, boolean skipSubject) {
    WORD last = lastWord(word);
    if((skipSubject && last == WORD.SUBJECT) || (last == WORD.DESCRIPTOR && last != prevWord))
      return lastWord(last) != prevWord;
    return last != prevWord;
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
      ScriptorMod.LOGGER.debug("No text provided to parser!");
      return null;
    }

    try {
      String[] tokens = text.split("\s");

      Subject subject = null;
      List<PartialSpell> spells = new ArrayList<>();

      Action action = null;
      List<Descriptor> descriptors = new ArrayList<>();

      String parsed;
      boolean subjectParsed = false;
      boolean skipSubject = false;
      WORD lastToken = null;
      int multiplier = 1;
      for(var token: tokens) {
        parsed = parseWord(token);

        if (parsed.startsWith("other:")) {
          if(parsed.equals("other:and")) {
            spells.add(new PartialSpell(action, descriptors.toArray(Descriptor[]::new)));
            action = null;
            descriptors = new ArrayList<>();
            lastToken = null;
            skipSubject = true;
          } else if (parsed.startsWith("other:x")) {
            if(lastToken != WORD.DESCRIPTOR) {
              LOGGER.debug("Failed to parse spell with text \"%s\"".formatted(text));
              LOGGER.debug("Can only duplicate descriptors!");
              return null;
            }
            System.out.println(parsed.substring(7));
            multiplier *= Integer.parseInt(parsed.substring(7));
          }
          continue;
        } else {
          if(multiplier > 1) {
            if(multiplier > 25) {
              multiplier = 25;

              LOGGER.info("Modified a parsed spell with text \"%s\"".formatted(text));
              LOGGER.info("Descriptor multiplier was reduced to %d".formatted(multiplier));
            }
            var duplicated = descriptors.get(descriptors.size() - 1);
            for(int i = 0; i < multiplier - 1; i++)
              descriptors.add(duplicated);
            multiplier = 1;
          }
        }

        if(parsed.startsWith("subject:")) {
          if(subjectParsed) {
            LOGGER.debug("Failed to parse spell with text \"%s\"".formatted(text));
            LOGGER.debug("Multiple subjects provided!");
            return null;
          }

          if(checkLastWord(WORD.SUBJECT, lastToken, skipSubject)) {
            LOGGER.debug("Failed to parse spell with text \"%s\"".formatted(text));
            LOGGER.debug("Subject in incorrect position!");
            return null;
          }

          subject = WordRegistry.INSTANCE.subjectRegistry.get(token.substring(8));
          subjectParsed = true;
          lastToken = WORD.SUBJECT;
        } else if (parsed.startsWith("action:")) {
          if(checkLastWord(WORD.ACTION, lastToken, skipSubject)) {
            LOGGER.debug("Failed to parse spell with text \"%s\"".formatted(text));
            LOGGER.debug("Action in incorrect position!");
            return null;
          }

          action = WordRegistry.INSTANCE.actionRegistry.get(token.substring(7));
          lastToken = WORD.ACTION;
        } else if (parsed.startsWith("descriptor:")) {
          if(checkLastWord(WORD.DESCRIPTOR, lastToken, skipSubject) && lastToken != WORD.DESCRIPTOR) {
            LOGGER.debug("Failed to parse spell with text \"%s\"".formatted(text));
            LOGGER.debug("Action in incorrect position!");
            return null;
          }

          descriptors.add(WordRegistry.INSTANCE.descriptorRegistry.get(token.substring(11)));
          lastToken = WORD.DESCRIPTOR;
        } else {
          LOGGER.warn("Failed to parse spell with text \"%s\"".formatted(text));
          LOGGER.warn("Encountered non-word \"%s\"".formatted(token));
          return null;
        }
      }

      if (action != null && subject != null) {
        spells.add(new PartialSpell(action, descriptors.toArray(Descriptor[]::new)));
        return new Spell(subject, spells.toArray(PartialSpell[]::new));
      }
    } catch (Exception e) {
      LOGGER.warn("==========================================================");
      LOGGER.warn("The below error did NOT cause a crash, this is debug info!");
      LOGGER.error("Error:", e);
      LOGGER.warn("==========================================================");
    }
    LOGGER.debug("Failed to process spell with text: \"" + text + "\"");
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
    if(spell != null) {
      List<String> list = new ArrayList<>();
      list.add(getKey(spell.subject()));
      for(var partialSpell: spell.spells()) {
        list.add(getKey(partialSpell.action()));
        for(var descriptor: partialSpell.descriptors())
          list.add(getKey(descriptor));
      }
      return list;
    }
    return null;
  }


  /**
   * A helper for generating a String to describe a spell.
   * @param spell The Spell to generate text for.
   * @return A String to describe a spell
   */
  public String generate(Spell spell) {
    assert spell.spells().length >= 1;
    StringBuilder descriptorBuilder = new StringBuilder();
    for(Descriptor descriptor: spell.spells()[0].deduplicatedDescriptors()) {
      descriptorBuilder.append(" ").append(getWord(descriptor));
    }

    StringBuilder builder = new StringBuilder();
    for(WORD w: spellStructure) {
      if(w == WORD.ACTION)
        builder.append(" ").append(getWord(spell.spells()[0].action()));
      else if(w == WORD.SUBJECT)
        builder.append(" ").append(getWord(spell.subject()));
      else if(w == WORD.DESCRIPTOR)
        builder.append(descriptorBuilder);
    }

    for(var partialSpell: Arrays.stream(spell.spells()).skip(1).toList()) {
      builder.append(" ").append(getWord("other:and"));
      descriptorBuilder = new StringBuilder();
      for(Descriptor descriptor: partialSpell.deduplicatedDescriptors()) {
        descriptorBuilder.append(" ").append(getWord(descriptor));
      }

      for(WORD w: spellStructure) {
        if(w == WORD.ACTION)
          builder.append(" ").append(getWord(partialSpell.action()));
        else if(w == WORD.DESCRIPTOR)
          builder.append(descriptorBuilder);
      }
    }

    return builder.toString().strip();
  }

  /**
   * A helper for generating a String to describe a spell.
   * @param spell The Spell resource to generate text for.
   * @return A String to describe a spell
   */
  public String generate(SpellResource spell) {
    StringBuilder builder = new StringBuilder();
    SpellResource.PartialSpellResource first = spell.spells.remove(0);

    for(var word: spellStructure) {
      if(word == WORD.ACTION)
        builder.append(getWord("action:" + first.action));
      else if (word == WORD.SUBJECT)
        builder.append(getWord("subject:" + spell.subject));
      else
        for(var either: first.descriptors) {
          if(either.isNumber())
            builder.append(getWord("other:x" + either.getAsNumber()));
          else
            builder.append(getWord("descriptor:" + either.getAsString()));
          builder.append(" ");
        }
      builder.append(" ");
    }
    return builder.toString();
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
    return server.getDataStorage().computeIfAbsent(
      new Factory<>(DictionarySavedData::new, DictionarySavedData::load, DataFixTypes.SAVED_DATA_MAP_DATA),
      ScriptorMod.COMMUNITY_MODE ? "scriptor_community_dictionary" : "scriptor_dictionary"
    );
  }

  @Override
  public String toString() {
    if(ScriptorMod.COMMUNITY_MODE)
      return "DictionarySavedData[COMMUNITY_MODE=true]";

    var builder = new StringBuilder();

    builder.append("Structure: ");
    for(var w: spellStructure)
      builder.append(w)
        .append(" ");

    builder.append("\nContains ")
      .append(words.size())
      .append(" Words.");

    return builder.toString();
  }
}
