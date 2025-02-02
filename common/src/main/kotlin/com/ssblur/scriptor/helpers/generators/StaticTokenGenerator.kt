package com.ssblur.scriptor.helpers.generators

import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ssblur.scriptor.error.MissingRequiredElementException
import com.ssblur.scriptor.registry.TokenGeneratorRegistry
import com.ssblur.scriptor.registry.TokenGeneratorRegistry.getGenerator
import java.lang.reflect.Type

class StaticTokenGenerator(obj: JsonObject?): TokenGenerator() {
  enum class CollisionStrategy {
    FAIL,
    FALLBACK,
    SHORTEN
  }

  @JvmRecord
  data class StaticTokenParameters(val collisionStrategy: CollisionStrategy?)

  var usedTokens: HashMap<String, Boolean> = HashMap()
  var parameters: StaticTokenParameters

  init {
    parameters = GSON.fromJson(obj, PARAMETERS_TYPE)
  }

  override fun generateToken(key: String, parameters: JsonObject?): String {
    checkNotNull(parameters)
    if (!parameters.has("token"))
      throw MissingRequiredElementException(
        "token",
        "The word \"$key\" must have a token defined because it uses a static token generator."
      )

    var token = parameters["token"].asString
    if (usedTokens.containsKey(token))
      when (this.parameters.collisionStrategy) {
        CollisionStrategy.FAIL -> throw RuntimeException(
          String.format("Failed to generate static token: token %s already in use!", token)
        )

        CollisionStrategy.SHORTEN -> token = shorten(token)
        CollisionStrategy.FALLBACK -> return getGenerator(TokenGeneratorRegistry.defaultGenerator)!!.generateToken(
          key,
          null
        )

        null -> {}
      }
    usedTokens[token] = true
    return token
  }

  fun shorten(string: String): String {
    var token = string
    while (usedTokens.containsKey(token)) {
      token = token.substring(0, token.length - 1)
      if (token.isEmpty()) throw RuntimeException(
        String.format(
          "Failed to generate a static token: token %s cannot be shortened any further!",
          string
        )
      )
    }
    return token
  }

  override fun canBeDefault(): Boolean {
    return false
  }

  companion object {
    var PARAMETERS_TYPE: Type = object: TypeToken<StaticTokenParameters?>() {}.type
    var GSON: Gson = Gson()
  }
}
