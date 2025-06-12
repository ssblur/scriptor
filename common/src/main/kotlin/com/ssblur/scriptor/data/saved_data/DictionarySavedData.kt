package com.ssblur.scriptor.data.saved_data

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.ssblur.scriptor.ScriptorMod.COMMUNITY_MODE
import com.ssblur.scriptor.ScriptorMod.LOGGER
import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.api.word.Subject
import com.ssblur.scriptor.api.word.Word
import com.ssblur.scriptor.registry.TokenGeneratorRegistry
import com.ssblur.scriptor.registry.words.WordRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.actionRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.descriptorRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.subjectRegistry
import com.ssblur.scriptor.word.PartialSpell
import com.ssblur.scriptor.word.Spell
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.datafix.DataFixTypes
import net.minecraft.world.level.Level
import net.minecraft.world.level.saveddata.SavedData
import java.util.*

class DictionarySavedData: SavedData {
  var spellStructure: MutableList<WORD?>
  var words: BiMap<String?, String>

  /**
   * A class for storing language data for a given world.
   * @param spellStructure The structure of spells for this world.
   * @param words All words which exist in this world's lexicon.
   */
  constructor(
    spellStructure: List<String?>,
    words: List<Pair<String?, String>>
  ) {
    this.spellStructure = spellStructure.stream().map { name: String? ->
      WORD.valueOf(
        name!!
      )
    }.toList()
    this.words = HashBiMap.create()
    for (pair in words) this.words[pair.first] = pair.second

    generateMissingWords()
    setDirty()
  }

  /**
   * An enum representing types of words used in Spells.
   */
  enum class WORD {
    ACTION,
    DESCRIPTOR,
    SUBJECT
  }


  fun containsKey(key: String?): Boolean {
    return words.containsKey(key)
  }

  fun containsWord(string: String): Boolean {
    return words.containsValue(string)
  }

  fun generateMissingWords() {
    val registry = TokenGeneratorRegistry
    var token: String

    if (!containsKey("other:and")) {
      do {
        token = registry.generateWord("other:and")
      } while (containsWord(token))
      words["other:and"] = token
    }

    for (word in actionRegistry.keys) {
      if (containsKey("action:$word")) continue

      do {
        token = registry.generateWord("action:$word")
      } while (containsWord(token))

      words["action:$word"] = token
    }

    for (word in descriptorRegistry.keys) {
      if (containsKey("descriptor:$word")) continue

      do {
        token = registry.generateWord("descriptor:$word")
      } while (containsWord(token))

      words["descriptor:$word"] = token
    }
    for (word in subjectRegistry.keys) {
      if (containsKey("subject:$word")) continue

      do {
        token = registry.generateWord("subject:$word")
      } while (containsWord(token))

      words["subject:$word"] = token
    }
  }

  constructor() {
    val basicStructure = arrayOf(WORD.SUBJECT, WORD.ACTION, WORD.DESCRIPTOR)
    val structure = Arrays.asList(*basicStructure)
    if (!COMMUNITY_MODE) Collections.shuffle(structure)

    spellStructure = ArrayList()
    spellStructure.addAll(structure)

    words = HashBiMap.create()
    generateMissingWords()

    setDirty()
  }

  /**
   * A helper for parsing a written word into corresponding WordData.
   * @param word The written word to parse
   * @return The matching word key
   * null if no matches
   */
  fun parseWord(word: String): String? {
    return words.inverse()[word]
  }

  /**
   * A helper for getting WordData based on its key.
   * @param key The key used to register a Word
   * @return The matching word
   * null if no matches
   */
  fun getWord(key: String?): String? {
    return words[key]
  }

  /**
   * A helper for getting a key based on a Word.
   * @param word The Word to search by
   * @return The matching key
   * null if no matches
   */
  fun getKey(word: Word?): String? {
    val key = WordRegistry.getKey(word)
    if (key != null) if (word is Action) return "action:$key"
    else if (word is Subject) return "subject:$key"
    else if (word is Descriptor) return "descriptor:$key"
    return null
  }


  /**
   * A helper for getting WordData based on a Word.
   * @param word The Word to search by
   * @return The matching word
   * null if no matches
   */
  fun getWord(word: Word?): String? {
    return getWord(getKey(word))
  }


  /**
   * Attempt to parse a String into a Spell
   * @param text The String to parse
   * @return The associated Spell
   * null if invalid
   */
  fun parse(text: String?): Spell? {
    if (text == null) {
      LOGGER.warn("No text provided to parser!")
      return null
    }
    var position = 0
    var tokenPosition = 0
    try {
      val tokens = text.split("[\\n\\r\\s]+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

      var subject: Subject? = null
      val spells: MutableList<PartialSpell> = ArrayList()

      var action: Action? = null
      val descriptors: MutableList<Descriptor> = ArrayList()

      var parsed: String?
      while (tokenPosition < tokens.size) {
        if (position % spellStructure.size == 0 && position > 0) {
          parsed = parseWord(tokens[tokenPosition])
          if (parsed != null && parsed == "other:and") {
            tokenPosition++
            spells.add(PartialSpell(action!!, *descriptors.toTypedArray()))
//          Reset descriptors list to allow precise control over which descriptors affect which action
            descriptors.clear()
          } else {
            return null
          }
        }
        if (position >= spellStructure.size && spellStructure[position % spellStructure.size] == WORD.SUBJECT) position++
        val word = spellStructure[position % spellStructure.size]
        val wordData = parseWord(tokens[tokenPosition])
        when (word) {
          WORD.ACTION -> {
            if (wordData == null) {
              LOGGER.debug("Failed to process spell with text: \"$text\"")
              LOGGER.debug("No word found for \"" + tokens[tokenPosition] + "\", action expected")
              return null
            }
            action = actionRegistry[wordData.substring(7)]
          }

          WORD.DESCRIPTOR -> {
            // Descriptors aren't required. If there are none, roll forward as necessary and continue.
            if (wordData == null || wordData.length < 12) {
              position++
              continue
            }
            val descriptor = descriptorRegistry[wordData.substring(11)]
            if (descriptor == null) {
              position++
              continue
            }
            descriptors.add(descriptor)

            tokenPosition++
            continue
          }

          WORD.SUBJECT -> {
            if (wordData == null) {
              LOGGER.debug("Failed to process spell with text: \"$text\"")
              LOGGER.debug("Subject " + tokens[tokenPosition] + " not found")
              return null
            }
            subject = subjectRegistry[wordData.substring(8)]
          }

          null -> {}
        }
        position++
        tokenPosition++
      }

      if (action != null && subject != null) {
        spells.add(PartialSpell(action, *descriptors.toTypedArray()))
        return Spell(subject, *spells.toTypedArray())
      }
    } catch (e: Exception) {
      LOGGER.warn("==========================================================")
      LOGGER.warn("The below error did NOT cause a crash, this is debug info!")
      LOGGER.error("Error:", e)
      LOGGER.warn("==========================================================")
    }
    LOGGER.debug("Failed to process spell with text: \"$text\"")
    return null
  }

  /**
   * Attempt to parse a String into lists of components
   * @param text The String to parse
   * @return The associated Spell
   * null if invalid
   */
  fun parseComponents(text: String?): List<String?>? {
    val spell = parse(text)
    if (spell != null) {
      val list: MutableList<String?> = ArrayList()
      list.add(getKey(spell.subject))
      for (partialSpell in spell.spells) {
        list.add(getKey(partialSpell.action))
        for (descriptor in partialSpell.descriptors) list.add(getKey(descriptor))
      }
      return list
    }
    return null
  }


  /**
   * A helper for generating a String to describe a spell.
   * @param spell The Spell to generate text for.
   * @return A String to describe a spell
   */
  fun generate(spell: Spell): String {
    assert(spell.spells.size >= 1)
    var descriptorBuilder = StringBuilder()
    for (descriptor in spell.spells[0].deduplicatedDescriptors()) {
      descriptorBuilder.append(" ").append(getWord(descriptor))
    }

    val builder = StringBuilder()
    for (w in spellStructure) {
      if (w == WORD.ACTION) builder.append(" ").append(
        getWord(
          spell.spells[0].action
        )
      )
      else if (w == WORD.SUBJECT) builder.append(" ").append(getWord(spell.subject))
      else if (w == WORD.DESCRIPTOR) builder.append(descriptorBuilder)
    }

    for (partialSpell in Arrays.stream<PartialSpell>(spell.spells).skip(1).toList()) {
      builder.append(" ").append(getWord("other:and"))
      descriptorBuilder = StringBuilder()
      for (descriptor in partialSpell.deduplicatedDescriptors()) {
        descriptorBuilder.append(" ").append(getWord(descriptor))
      }

      for (w in spellStructure) {
        if (w == WORD.ACTION) builder.append(" ").append(getWord(partialSpell.action))
        else if (w == WORD.DESCRIPTOR) builder.append(descriptorBuilder)
      }
    }

    return builder.toString().trim()
  }

  override fun save(tag: CompoundTag, provider: HolderLookup.Provider): CompoundTag {
    worldCodec.encodeStart(NbtOps.INSTANCE, this).ifSuccess { tag.put("scriptor:dictionary", it) }
    return tag
  }

  override fun toString(): String {
    if (COMMUNITY_MODE) return "DictionarySavedData[COMMUNITY_MODE=true]"

    val builder = StringBuilder()

    builder.append("Structure: ")
    for (w in spellStructure) builder.append(w)
      .append(" ")

    builder.append("\nContains ")
      .append(words.size)
      .append(" Words.")

    return builder.toString()
  }

  companion object {
    val worldCodec: Codec<DictionarySavedData?> =
      RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<DictionarySavedData?> ->
        instance.group(
          Codec.STRING.listOf().fieldOf("spellStructure").forGetter { worldData: DictionarySavedData? ->
            worldData!!.spellStructure.stream().map { obj: WORD? -> obj.toString() }
              .toList()
          },
          Codec.compoundList(Codec.STRING, Codec.STRING)
            .fieldOf("words")
            .forGetter {
              it?.let {
                it.words.keys.stream().map { key: String? -> Pair(key, it.words[key]) }.toList()
              }
            }
        ).apply(instance, ::DictionarySavedData)
      }

    fun load(tag: CompoundTag, @Suppress("unused_parameter") provider: HolderLookup.Provider?): DictionarySavedData? {
      val input = tag["scriptor:dictionary"]
      if (input != null) {
        val result = worldCodec.decode(NbtOps.INSTANCE, input).result()
        if (result.isPresent && result.get().first != null) return result.get().first
      }
      return null
    }

    /**
     * A helper for calling computeIfAbsent for this class from the Overworld
     * @param level Any ServerLevel
     * @return The DictionarySavedData for this world
     */
    @JvmStatic
    fun computeIfAbsent(level: ServerLevel): DictionarySavedData {
      val server = level.server.getLevel(Level.OVERWORLD)
      Objects.requireNonNull(server)
      return server!!.dataStorage.computeIfAbsent(
        Factory(
          { DictionarySavedData() },
          { tag: CompoundTag, provider: HolderLookup.Provider? -> load(tag, provider) },
          DataFixTypes.SAVED_DATA_MAP_DATA
        ),
        if (COMMUNITY_MODE) "scriptor_community_dictionary" else "scriptor_dictionary"
      )
    }
  }
}
