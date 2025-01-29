package com.ssblur.scriptor.resources

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.color.CustomColors.registerWithEasing
import com.ssblur.scriptor.registry.words.WordRegistry.register
import com.ssblur.scriptor.word.descriptor.color.CustomColorDescriptor
import com.ssblur.unfocused.data.DataLoaderRegistry.registerDataLoader
import oshi.util.tuples.Triplet

object Colors {
  data class Color(val color: List<String>)

  var cache: List<Triplet<Int, String, IntArray>> = mutableListOf()

  init {
    ScriptorMod.registerDataLoader("scriptor/colors", Color::class) { color, location ->
      val colors = color.color.map { Integer.parseInt(it, 16) }.toIntArray()
      val index = registerWithEasing(location.toShortLanguageKey(), colors)
      val name = location.toShortLanguageKey()
      cache += Triplet(index, name, colors)
      register("color.$name", CustomColorDescriptor(name))
      ScriptorMod.LOGGER.info("Registering custom color $name")
    }
  }
}