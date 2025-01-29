package com.ssblur.scriptor.resources

import com.google.gson.JsonObject
import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.ScriptorMod.COMMUNITY_MODE
import com.ssblur.scriptor.ScriptorMod.LOGGER
import com.ssblur.scriptor.error.InvalidGeneratorException
import com.ssblur.scriptor.helpers.generators.CommunityModeGenerator
import com.ssblur.scriptor.registry.TokenGeneratorRegistry
import com.ssblur.scriptor.registry.TokenGeneratorRegistry.defaultGenerator
import com.ssblur.scriptor.registry.TokenGeneratorRegistry.getGeneratorGenerator
import com.ssblur.scriptor.registry.TokenGeneratorRegistry.registerDefaultGenerator
import com.ssblur.scriptor.registry.TokenGeneratorRegistry.registerGenerator
import com.ssblur.unfocused.data.DataLoaderRegistry.registerDataLoader

object Generators {
  data class Generator(val generator: String, val parameters: JsonObject, val default: Boolean?)

  init {
    ScriptorMod.registerDataLoader("scriptor/generators", Generator::class) { generator, location ->
      if (getGeneratorGenerator(generator.generator) == null) throw InvalidGeneratorException(
        generator.generator,
        location
      )

      if (generator.generator == "community") {
        LOGGER.info("Community mode generator loaded, locking down debug features.")
        COMMUNITY_MODE = true
      } else if (TokenGeneratorRegistry.generators.all { it.value !is CommunityModeGenerator }) COMMUNITY_MODE = false

      val generatorGenerator = getGeneratorGenerator(generator.generator)
      if (generatorGenerator != null) registerGenerator(location, generatorGenerator.create(generator.parameters))
      if (generator.default == true)
        if (defaultGenerator == null || defaultGenerator!!.namespace == "scriptor" || location.namespace != "scriptor")
          registerDefaultGenerator(location)
        else LOGGER.warn("Skipping registration of default generator at $location; A default generator outside the 'scriptor' namespace was already registered.")
    }
  }
}