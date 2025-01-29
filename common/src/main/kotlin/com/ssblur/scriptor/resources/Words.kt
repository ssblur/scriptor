package com.ssblur.scriptor.resources

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.ScriptorMod.LOGGER
import com.ssblur.scriptor.registry.words.WordRegistry.register
import com.ssblur.scriptor.word.action.CommandAction
import com.ssblur.unfocused.data.DataLoaderRegistry.registerDataLoader

object Words {
  data class Word(
    val cost: Int,
    val castAtPosition: List<String>,
    val castOnEntity: List<String>,
    val castOnItem: List<String>
  )

  init {
    ScriptorMod.registerDataLoader("scriptor/actions", Word::class) { word, location ->
      LOGGER.info("Loaded word {}. Cost: {}", location.toShortLanguageKey(), word.cost)
      register(
        "action." + location.toShortLanguageKey(),
        CommandAction(
          word.cost.toDouble(),
          word.castAtPosition.toTypedArray(),
          word.castOnEntity.toTypedArray(),
          word.castOnItem.toTypedArray()
        )
      )
    }
  }
}