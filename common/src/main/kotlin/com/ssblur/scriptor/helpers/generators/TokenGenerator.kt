package com.ssblur.scriptor.helpers.generators

import com.google.gson.JsonObject

abstract class TokenGenerator {
  abstract fun generateToken(key: String, parameters: JsonObject?): String
  abstract fun canBeDefault(): Boolean

  fun interface TokenGeneratorGenerator {
    fun create(parameters: JsonObject): TokenGenerator
  }
}
