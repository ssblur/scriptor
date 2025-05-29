package com.ssblur.scriptor.registry.words

import com.google.common.collect.HashBiMap
import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.api.word.Subject
import com.ssblur.scriptor.api.word.Word
import com.ssblur.unfocused.config.Config

@Suppress("unused")
object WordRegistry {
  val actionRegistry: HashBiMap<String, Action> = HashBiMap.create()
  val descriptorRegistry: HashBiMap<String, Descriptor> = HashBiMap.create()
  val subjectRegistry: HashBiMap<String, Subject> = HashBiMap.create()

  val actionCost: HashMap<String, Word.Cost> = HashMap()
  val descriptorCost: HashMap<String, Word.Cost> = HashMap()
  val subjectCost: HashMap<String, Word.Cost> = HashMap()

  /**
   * Tries to get find a word's key in the relevant registry.
   * @param word The word to search for
   * @return The associated key
   */
  fun getKey(word: Word?) =
    when (word) {
      is Action -> actionRegistry.inverse()[word]
      is Descriptor -> descriptorRegistry.inverse()[word]
      is Subject -> subjectRegistry.inverse()[word]
      else -> null
    }

  /**
   * Register a new Action.
   * @param key The key at which to register this word
   * @param action The word to register
   */
  fun register(key: String, action: Action) = actionRegistry.put(key, action)

  /**
   * Register a new Descriptor.
   * @param key The key at which to register this word
   * @param descriptor The word to register
   */
  fun register(key: String, descriptor: Descriptor) = descriptorRegistry.put(key, descriptor)

  /**
   * Register a new Subject.
   * @param key The key at which to register this word
   * @param subject The word to register
   */
  fun register(key: String, subject: Subject) = subjectRegistry.put(key, subject)

  init {
    Actions
    PotionActions
    ColorDescriptors
    Descriptors
    OffsetDescriptors
    InventoryDescriptors
    MultiTargetFocusDescriptors
    DiscountDescriptors
    PowerDescriptors
    Subjects

    val config = Config("scriptor_cost", "=", "## Allows configuring costs for all default words in Scriptor\n\n")
    actionRegistry.forEach { (key, value) ->
      val cost = value.cost()
      val type =
        Word.COSTTYPE.valueOf(config.get("action.$key.type", cost.type.toString(), "Cost type to use for $key"))
      val number = config.get("action.$key.cost", cost.cost.toString(), "Cost to cast for $key").toDouble()

      actionCost[key] = Word.Cost(number, type)
    }
    subjectRegistry.forEach { (key, value) ->
      val cost = value.cost()
      val type =
        Word.COSTTYPE.valueOf(config.get("subject.$key.type", cost.type.toString(), "Cost type to use for $key"))
      val number = config.get("subject.$key.cost", cost.cost.toString(), "Cost to cast for $key").toDouble()
      subjectCost[key] = Word.Cost(number, type)
    }
    descriptorRegistry.forEach { (key, value) ->
      val cost = value.cost()
      val type =
        Word.COSTTYPE.valueOf(config.get("descriptor.$key.type", cost.type.toString(), "Cost type to use for $key"))
      val number = config.get("descriptor.$key.cost", cost.cost.toString(), "Cost to cast for $key").toDouble()
      descriptorCost[key] = Word.Cost(number, type)
    }
    config.save()
  }
}
