package com.ssblur.scriptor.registry

import com.google.gson.JsonObject
import com.ssblur.scriptor.helpers.generators.*
import com.ssblur.scriptor.helpers.generators.TokenGenerator.TokenGeneratorGenerator
import net.minecraft.resources.ResourceLocation

object TokenGeneratorRegistry {
    var generators: HashMap<ResourceLocation?, TokenGenerator> = HashMap()
    var generatorBindings: HashMap<String, ResourceLocation?> = HashMap()
    var generatorBindingConfig: HashMap<String, JsonObject?> = HashMap()
    var generatorGenerators: HashMap<String, TokenGeneratorGenerator> = HashMap()
    @JvmField
    var defaultGenerator: ResourceLocation? = null

    fun registerGeneratorGenerator(key: String, generatorGenerator: TokenGeneratorGenerator): TokenGeneratorGenerator {
        generatorGenerators[key] = generatorGenerator
        return generatorGenerator
    }

    fun getGeneratorGenerator(key: String): TokenGeneratorGenerator? {
        return generatorGenerators[key]
    }

    fun registerGenerator(key: ResourceLocation?, generator: TokenGenerator) {
        generators[key] = generator
    }

    fun getGenerator(key: ResourceLocation?): TokenGenerator? {
        return generators[key]
    }

    fun registerBinding(word: String, generator: ResourceLocation?, parameters: JsonObject?) {
        generatorBindings[word] = generator
        generatorBindingConfig[word] = parameters
    }

    fun getBinding(word: String): ResourceLocation? {
        return generatorBindings.getOrDefault(word, defaultGenerator)
    }

    fun registerDefaultGenerator(generator: ResourceLocation?) {
        defaultGenerator = generator
    }

    @JvmOverloads
    fun generateWord(word: String, `object`: JsonObject? = generatorBindingConfig[word]): String {
        return getGenerator(getBinding(word))!!.generateToken(word, `object`)
    }

    val MIXED_GROUP: TokenGeneratorGenerator = registerGeneratorGenerator("mixed_groups", TokenGeneratorGenerator { MixedGroupGenerator(it) })
    val STATIC_TOKEN: TokenGeneratorGenerator = registerGeneratorGenerator("static_token", TokenGeneratorGenerator { StaticTokenGenerator(it) })
    val COMMUNITY: TokenGeneratorGenerator = registerGeneratorGenerator("community", TokenGeneratorGenerator { CommunityModeGenerator(it) })
    val DEBUG: TokenGeneratorGenerator = registerGeneratorGenerator("debug", TokenGeneratorGenerator { DebugGenerator(it) })
}
