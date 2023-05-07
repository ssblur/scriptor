package com.ssblur.scriptor.helpers;

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
  public List<String> actionGenderedArticles;
  public ARTICLEPOSITION actionArticlePosition;
  public List<String> descriptorGenderedArticles;
  public ARTICLEPOSITION descriptorArticlePosition;
  public List<String> subjectGenderedArticles;
  public ARTICLEPOSITION subjectArticlePosition;
  public List<WordData> words;
  /**
   * A class for storing language data for a given world.
   * @param spellStructure The structure of spells for this world.
   * @param actionGenderedArticles All gendered articles which exist for actions in this world.
   * @param actionArticlePosition Position and presence of an article for the action a spell describes.
   * @param descriptorGenderedArticles All gendered articles which exist for descriptors in this world.
   * @param descriptorArticlePosition Position and presence of an article for the descriptors of a spell.
   * @param subjectGenderedArticles All gendered articles which exist for subjects in this world.
   * @param subjectArticlePosition Position and presence of an article for the subject of a spell.
   * @param words All words which exist in this world's lexicon.
   */
  public DictionarySavedData(
    List<WORD> spellStructure,
    List<String> actionGenderedArticles,
    ARTICLEPOSITION actionArticlePosition,
    List<String> descriptorGenderedArticles,
    ARTICLEPOSITION descriptorArticlePosition,
    List<String> subjectGenderedArticles,
    ARTICLEPOSITION subjectArticlePosition,
    List<WordData> words
  ) {
    this.spellStructure = spellStructure;
    this.actionGenderedArticles = actionGenderedArticles;
    this.actionArticlePosition = actionArticlePosition;
    this.descriptorGenderedArticles = descriptorGenderedArticles;
    this.descriptorArticlePosition = descriptorArticlePosition;
    this.subjectGenderedArticles = subjectGenderedArticles;
    this.subjectArticlePosition = subjectArticlePosition;
    this.words = new ArrayList<>(words);

    generateMissingWords();
    setDirty();
  }

  public DictionarySavedData(
    List<String> spellStructure,
    List<String> actionGenderedArticles,
    String actionArticlePosition,
    List<String> descriptorGenderedArticles,
    String descriptorArticlePosition,
    List<String> subjectGenderedArticles,
    String subjectArticlePosition,
    List<WordData> words
  ) {
    this(
      spellStructure.stream().map(WORD::valueOf).toList(),
      actionGenderedArticles,
      ARTICLEPOSITION.valueOf(actionArticlePosition),
      descriptorGenderedArticles,
      ARTICLEPOSITION.valueOf(descriptorArticlePosition),
      subjectGenderedArticles,
      ARTICLEPOSITION.valueOf(subjectArticlePosition),
      words
    );
  }

  /**
   * An enum representing types of words used in Spells.
   */
  public enum WORD {
    PREFIXARTICLE,
    SUFFIXARTICLE,
    ACTION,
    DESCRIPTOR,
    SUBJECT
  }

  /**
   * An enum representing the positions of an article relative to a Word.
   */
  public enum ARTICLEPOSITION {
    NONE,
    BEFORE,
    AFTER
  }

  /**
   * Record for representing individual words.
   * @param word The word as read in-game
   * @param key The key representing the original word
   * @param gender The gender associated with this word
   */
  public record WordData(
    String word,
    String key,
    int gender
  ){}

  public static final Codec<WordData> wordCodec = RecordCodecBuilder.create(instance -> instance.group(
    Codec.STRING.fieldOf("word").forGetter(WordData::word),
    Codec.STRING.fieldOf("key").forGetter(WordData::key),
    Codec.INT.fieldOf("gender").forGetter(WordData::gender)
  ).apply(instance, WordData::new));

  public static final Codec<DictionarySavedData> worldCodec = RecordCodecBuilder.create(instance -> instance.group(
    Codec.STRING.listOf().fieldOf("spellStructure").forGetter(worldData -> worldData.spellStructure.stream().map(WORD::toString).toList()),
    Codec.STRING.listOf().fieldOf("actionGenderedArticles").forGetter(worldData -> worldData.actionGenderedArticles.stream().toList()),
    Codec.STRING.fieldOf("actionArticlePosition").forGetter(worldData -> worldData.actionArticlePosition.toString()),
    Codec.STRING.listOf().fieldOf("descriptorGenderedArticles").forGetter(worldData -> worldData.descriptorGenderedArticles.stream().toList()),
    Codec.STRING.fieldOf("descriptorArticlePosition").forGetter(worldData -> worldData.descriptorArticlePosition.toString()),
    Codec.STRING.listOf().fieldOf("subjectGenderedArticles").forGetter(worldData -> worldData.subjectGenderedArticles.stream().toList()),
    Codec.STRING.fieldOf("subjectArticlePosition").forGetter(worldData -> worldData.subjectArticlePosition.toString()),
    wordCodec.listOf().fieldOf("words").forGetter(worldData -> worldData.words.stream().toList())
  ).apply(instance, DictionarySavedData::new));

  boolean containsKey(String key) {
    for(var word: words)
      if(word.key.equalsIgnoreCase(key))
        return true;
    return false;
  }

  boolean containsWord(String string) {
    for(var word: words)
      if(word.word.equalsIgnoreCase(string))
        return true;
    return false;
  }

  void generateMissingWords() {
    TokenGenerator generator = DefaultTokenGenerators.angGenerator;
    String token;
    int genders = actionGenderedArticles.size();
    Random random = new Random();

    for(var word: WordRegistry.INSTANCE.actionRegistry.keySet()) {
      if(containsKey("action:" + word))
        continue;

      do {
        token = generator.generateToken();
      } while (containsWord(token));

      words.add(new WordData(
        generator.generateToken(),
        "action:" + word,
        random.nextInt(genders)
      ));
    }

    for(var word: WordRegistry.INSTANCE.descriptorRegistry.keySet()) {
      if(containsKey("descriptor:" + word))
        continue;

      do {
        token = generator.generateToken();
      } while (containsWord(token));

      words.add(new WordData(
        generator.generateToken(),
        "descriptor:" + word,
        random.nextInt(genders)
      ));
    }
    for(var word: WordRegistry.INSTANCE.subjectRegistry.keySet()) {
      if(containsKey("subject:" + word))
        continue;

      do {
        token = generator.generateToken();
      } while (containsWord(token));

      words.add(new WordData(
        generator.generateToken(),
        "subject:" + word,
        random.nextInt(genders)
      ));
    }
  }

  public DictionarySavedData() {
    WORD[] basicStructure = new WORD[]{WORD.ACTION, WORD.DESCRIPTOR, WORD.SUBJECT};
    List<WORD> structure = Arrays.asList(basicStructure);
    Collections.shuffle(structure);

    String token;
    Random random = new Random();
    TokenGenerator shortGenerator = DefaultTokenGenerators.shortAngGenerator;
    int genders = random.nextInt(3) + 1;

    actionArticlePosition = ARTICLEPOSITION.values()[random.nextInt(ARTICLEPOSITION.values().length)];
    descriptorArticlePosition = ARTICLEPOSITION.values()[random.nextInt(ARTICLEPOSITION.values().length)];
    subjectArticlePosition = ARTICLEPOSITION.values()[random.nextInt(ARTICLEPOSITION.values().length)];

    actionGenderedArticles = new ArrayList<>();
    for(int i = 0; i < genders; i++) {
      do
        token = shortGenerator.generateToken();
      while (actionGenderedArticles.contains(token));
      actionGenderedArticles.add(token);
    }

    descriptorGenderedArticles = new ArrayList<>();
    for(int i = 0; i < genders; i++) {
      do
        token = shortGenerator.generateToken();
      while (descriptorGenderedArticles.contains(token));
      descriptorGenderedArticles.add(token);
    }

    subjectGenderedArticles = new ArrayList<>();
    for(int i = 0; i < genders; i++) {
      do
        token = shortGenerator.generateToken();
      while (subjectGenderedArticles.contains(token));
      subjectGenderedArticles.add(token);
    }

    spellStructure = new ArrayList<>();
    for(WORD w: structure) {
      if(w == WORD.ACTION) {
        if(actionArticlePosition == ARTICLEPOSITION.BEFORE)
          spellStructure.add(WORD.PREFIXARTICLE);
        spellStructure.add(w);
        if(actionArticlePosition == ARTICLEPOSITION.AFTER)
          spellStructure.add(WORD.SUFFIXARTICLE);
      } else if(w == WORD.SUBJECT) {
        if(subjectArticlePosition == ARTICLEPOSITION.BEFORE)
          spellStructure.add(WORD.PREFIXARTICLE);
        spellStructure.add(w);
        if(subjectArticlePosition == ARTICLEPOSITION.AFTER)
          spellStructure.add(WORD.SUFFIXARTICLE);
      } else {
        if(descriptorArticlePosition == ARTICLEPOSITION.BEFORE)
          spellStructure.add(WORD.PREFIXARTICLE);
        spellStructure.add(w);
        if(descriptorArticlePosition == ARTICLEPOSITION.AFTER)
          spellStructure.add(WORD.SUFFIXARTICLE);
      }
    }

    words = new ArrayList<>();
    generateMissingWords();

    setDirty();
  }

  /**
   * A helper for parsing a written word into corresponding WordData.
   * @param word The written word to parse
   * @return WordData with a matching word field
   * null if no matches
   */
  public WordData parseWord(String word) {
    for(WordData wordData: words)
      if (wordData.word.equalsIgnoreCase(word))
        return wordData;
    return null;
  }

  /**
   * A helper for getting WordData based on its key.
   * @param key The key used to register a Word
   * @return The WordData associated with a key
   * null if no matches
   */
  public WordData getWord(String key) {
    for(WordData wordData: words)
      if (wordData.key.equalsIgnoreCase(key))
        return wordData;
    return null;
  }

  /**
   * A helper for getting WordData based on a Word.
   * @param action The Action to search by
   * @return The WordData associated with a Word
   * null if no matches
   */
  public WordData getWord(Action action) {
    for(String key: WordRegistry.INSTANCE.actionRegistry.keySet()) {
      if(action == WordRegistry.INSTANCE.actionRegistry.get(key))
        return getWord("action:" + key);
    }
    return null;
  }

  /**
   * A helper for getting WordData based on a Word.
   * @param descriptor The Descriptor to search by
   * @return The WordData associated with a Word
   * null if no matches
   */
  public WordData getWord(Descriptor descriptor) {
    for(String key: WordRegistry.INSTANCE.descriptorRegistry.keySet()) {
      if(descriptor == WordRegistry.INSTANCE.descriptorRegistry.get(key))
        return getWord("descriptor:" + key);
    }
    return null;
  }

  /**
   * A helper for getting WordData based on a Word.
   * @param subject The Subject to search by
   * @return The WordData associated with a Word
   * null if no matches
   */
  public WordData getWord(Subject subject) {
    for(String key: WordRegistry.INSTANCE.subjectRegistry.keySet()) {
      if(subject == WordRegistry.INSTANCE.subjectRegistry.get(key))
        return getWord("subject:" + key);
    }
    return null;
  }

  /**
   * Attempts to get the gender of a given article
   * @param word The Word type to search by
   * @param article The article to search for
   * @return The gender associated with an article
   * -1 if no matches
   */
  public int parseArticle(WORD word, String article) {
    if(word == WORD.ACTION) {
      for (int i = 0; i < actionGenderedArticles.size(); i++)
        if (actionGenderedArticles.get(i).equalsIgnoreCase(article))
          return i;
    } else if(word == WORD.DESCRIPTOR) {
      for (int i = 0; i < descriptorGenderedArticles.size(); i++)
        if (descriptorGenderedArticles.get(i).equalsIgnoreCase(article))
          return i;
    } else if(word == WORD.SUBJECT) {
      for (int i = 0; i < subjectGenderedArticles.size(); i++)
        if (subjectGenderedArticles.get(i).equalsIgnoreCase(article))
          return i;
    }
    return -1;
  }

  boolean hasPrefix(WORD word) {
    if(word == WORD.ACTION)
      return actionArticlePosition == ARTICLEPOSITION.BEFORE;
    if(word == WORD.SUBJECT)
      return subjectArticlePosition == ARTICLEPOSITION.BEFORE;
    if(word == WORD.DESCRIPTOR)
      return descriptorArticlePosition == ARTICLEPOSITION.BEFORE;
    return false;
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
        WordData wordData;
        switch(word) {
          case PREFIXARTICLE:
            if(spellStructure.get(position + 1) == WORD.DESCRIPTOR && !parseWord(tokens[tokenPosition + 1]).key.startsWith("descriptor")) {
              position++;
              continue;
            }
            if (
              (position + 1) > spellStructure.size()
                || parseArticle(spellStructure.get(position + 1), tokens[tokenPosition]) != parseWord(tokens[tokenPosition + 1]).gender) {
              ScriptorMod.LOGGER.debug("Failed to process spell with text: \"" + text + "\"");
              ScriptorMod.LOGGER.debug("Article \"" + tokens[tokenPosition] + "\" not valid for \"" + tokens[tokenPosition + 1] + "\"");
              return null;
            }
            break;
          case SUFFIXARTICLE:
            if (parseArticle(spellStructure.get(position - 1), tokens[tokenPosition]) != parseWord(tokens[tokenPosition - 1]).gender) {
              ScriptorMod.LOGGER.debug("Failed to process spell with text: \"" + text + "\"");
              ScriptorMod.LOGGER.debug("Article \"" + tokens[tokenPosition] + "\" not valid for \"" + tokens[tokenPosition - 1] + "\"");
              return null;
            }
            break;
          case ACTION:
            wordData = parseWord(tokens[tokenPosition]);
            if(wordData == null) {
              ScriptorMod.LOGGER.debug("Failed to process spell with text: \"" + text + "\"");
              ScriptorMod.LOGGER.debug("No word found for \"" + tokens[tokenPosition] + "\", action expected");
              return null;
            }
            action = WordRegistry.INSTANCE.actionRegistry.get(wordData.key.substring(7));
            break;
          case DESCRIPTOR:
            wordData = parseWord(tokens[tokenPosition]);
            // Descriptors aren't required. If there is none, roll forward as necessary and continue.
            if (wordData == null) {
              position++;
              if(descriptorArticlePosition == ARTICLEPOSITION.AFTER)
                position++;
              continue;
            }
            Descriptor descriptor = WordRegistry.INSTANCE.descriptorRegistry.get(wordData.key.substring(11));
            if (descriptor == null) {
              position++;
              continue;
            }
            descriptors.add(descriptor);

            // If there are enough tokens to have more descriptors, process descriptors again.
            if ((tokens.length - tokenPosition) > (spellStructure.size() - position))
              if (descriptorArticlePosition == ARTICLEPOSITION.NONE)
                position--;
              else if (descriptorArticlePosition == ARTICLEPOSITION.BEFORE)
                position -= 2;
              else {
                position++;
                tokenPosition++;
                if (parseArticle(spellStructure.get(position - 1), tokens[tokenPosition]) != parseWord(tokens[tokenPosition - 1]).gender) {
                  ScriptorMod.LOGGER.debug("Failed to process spell with text: \"" + text + "\"");
                  ScriptorMod.LOGGER.debug("Article for descriptor incorrect");
                  return null;
                }
                position -= 2;
              }
            break;
          case SUBJECT:
            wordData = parseWord(tokens[tokenPosition]);
            if(wordData == null) {
              ScriptorMod.LOGGER.debug("Failed to process spell with text: \"" + text + "\"");
              ScriptorMod.LOGGER.debug("Subject " + tokens[tokenPosition] + " not found");
              return null;
            }
            subject = WordRegistry.INSTANCE.subjectRegistry.get(wordData.key.substring(8));
            break;
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
    try {
      int position = 0;
      int tokenPosition = 0;
      String[] tokens = text.split("\s");

      List<String> words = new ArrayList<>();

      while (position < spellStructure.size() && tokenPosition < tokens.length) {
        WORD word = spellStructure.get(position);
        WordData wordData;
        switch(word) {
          case PREFIXARTICLE:
            if(spellStructure.get(position + 1) == WORD.DESCRIPTOR && !parseWord(tokens[tokenPosition + 1]).key.startsWith("descriptor")) {
              position++;
              continue;
            }
            if (
              (position + 1) > spellStructure.size()
                || parseArticle(spellStructure.get(position + 1), tokens[tokenPosition]) != parseWord(tokens[tokenPosition + 1]).gender)
              return null;
            break;
          case SUFFIXARTICLE:
            if (parseArticle(spellStructure.get(position - 1), tokens[tokenPosition]) != parseWord(tokens[tokenPosition - 1]).gender)
              return null;
            break;
          case ACTION:
          case SUBJECT:
            wordData = parseWord(tokens[tokenPosition]);
            if(wordData == null)
              return null;
            words.add(wordData.key);
            break;
          case DESCRIPTOR:
            wordData = parseWord(tokens[tokenPosition]);
            // Descriptors aren't required. If there is none, roll forward as necessary and continue.
            if (wordData == null) {
              position++;
              if(descriptorArticlePosition == ARTICLEPOSITION.AFTER)
                position++;
              continue;
            }
            Descriptor descriptor = WordRegistry.INSTANCE.descriptorRegistry.get(wordData.key.substring(11));
            if (descriptor == null) {
              position++;
              continue;
            }
            words.add(wordData.key);

            // If there are enough tokens to have more descriptors, process descriptors again.
            if ((tokens.length - tokenPosition) > (spellStructure.size() - position))
              if (descriptorArticlePosition == ARTICLEPOSITION.NONE)
                position--;
              else if (descriptorArticlePosition == ARTICLEPOSITION.BEFORE)
                position -= 2;
              else {
                position++;
                tokenPosition++;
                if (parseArticle(spellStructure.get(position - 1), tokens[tokenPosition]) != parseWord(tokens[tokenPosition - 1]).gender)
                  return null;
                position -= 2;
              }
            break;
        }

        position++;
        tokenPosition++;
      }
      return words;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  /**
   * A helper for generating a String to describe a spell.
   * @param spell The Spell to generate text for.
   * @return A String to describe a spell
   */
  public String generate(Spell spell) {
    String actionString;
    WordData actionData = getWord(spell.action());
    if(actionArticlePosition == ARTICLEPOSITION.BEFORE)
      actionString = actionGenderedArticles.get(actionData.gender) + " " + actionData.word;
    else if (actionArticlePosition == ARTICLEPOSITION.AFTER)
      actionString = actionData.word + " " + actionGenderedArticles.get(actionData.gender);
    else actionString = actionData.word;

    String subjectString;
    WordData subjectData = getWord(spell.subject());
    if(subjectArticlePosition == ARTICLEPOSITION.BEFORE)
      subjectString = subjectGenderedArticles.get(subjectData.gender) + " " + subjectData.word;
    else if (subjectArticlePosition == ARTICLEPOSITION.AFTER)
      subjectString = subjectData.word + " " + subjectGenderedArticles.get(subjectData.gender);
    else subjectString = subjectData.word;

    StringBuilder descriptorBuilder = new StringBuilder();
    for(Descriptor descriptor: spell.deduplicatedDescriptors()) {
      WordData descriptorData = getWord(descriptor);
      if(descriptorArticlePosition == ARTICLEPOSITION.BEFORE)
        descriptorBuilder.append(" ").append(descriptorGenderedArticles.get(descriptorData.gender)).append(" ").append(descriptorData.word);
      else if (descriptorArticlePosition == ARTICLEPOSITION.AFTER)
        descriptorBuilder.append(" ").append(descriptorData.word).append(" ").append(descriptorGenderedArticles.get(descriptorData.gender));
      else descriptorBuilder.append(" ").append(descriptorData.word);
    }

    StringBuilder builder = new StringBuilder();
    for(WORD w: spellStructure) {
      if(w == WORD.ACTION)
        builder.append(" ").append(actionString);
      else if(w == WORD.SUBJECT)
        builder.append(" ").append(subjectString);
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

    for(var w: spellStructure)
      builder.append(w).append(" ");
    builder.append("\n\n");

    builder.append("Words:\n");
    for(var w: words) {
      builder.append('"').append(w.key).append('"');
      builder.append(" : ");
      builder.append(w.word);
      builder.append(" (").append(w.gender).append(")\n");
    }

    builder.append("\nAction Articles:\n");
    for(var w: actionGenderedArticles)
      builder.append(w).append("\n");

    builder.append("\nSubject Articles:\n");
    for(var w: subjectGenderedArticles)
      builder.append(w).append("\n");

    builder.append("\nDescriptor Articles:\n");
    for(var w: descriptorGenderedArticles)
      builder.append(w).append("\n");

    return builder.toString();
  }
}
