package com.ssblur.scriptor.helpers.generators

import com.google.common.reflect.TypeToken
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import java.lang.reflect.Type
import java.util.*

class MixedGroupGenerator(obj: JsonObject?): TokenGenerator() {
  @JvmRecord
  data class TokenGroup(val tokens: Array<String>, val weight: Int)

  @JvmRecord
  data class MixedGroupParameters(
    val groups: Array<TokenGroup>,
    val maxConsecutiveGroups: Int,
    val minTokens: Int,
    val maxTokens: Int
  )

  var totalWeight: Int = 0
  var parameters: MixedGroupParameters? = null

  init {
    parameters = GSON.fromJson(obj, PARAMETERS_TYPE)
    parameters?.let { for ((_, weight) in it.groups) totalWeight += weight }
  }

  override fun canBeDefault(): Boolean {
    return true
  }

  override fun generateToken(key: String, parameters: JsonObject?): String {
    var maxTokens = this.parameters!!.maxTokens
    var minTokens = this.parameters!!.minTokens
    var maxConsecutiveGroups = this.parameters!!.maxConsecutiveGroups

    if (this.parameters!!.groups.size <= 1) maxConsecutiveGroups = 0

    if (parameters != null) {
      if (parameters.has("max_tokens")) maxTokens = parameters["max_tokens"].asInt
      if (parameters.has("min_tokens")) minTokens = parameters["min_tokens"].asInt
    }

    val tokens = RANDOM.nextInt(minTokens, maxTokens + 1)
    val builder = StringBuilder()

    var lastGroup: TokenGroup? = null
    var consecutiveGroups = 0

    for (i in 0 until tokens) {
      var tokenGroup: TokenGroup? = null
      do {
        var random = RANDOM.nextInt(totalWeight)
        for (group in this.parameters!!.groups) {
          if (random < group.weight) {
            tokenGroup = group
            break
          } else {
            random -= group.weight
          }
        }
      } while (tokenGroup == null
        || (maxConsecutiveGroups > 0
            && (lastGroup === tokenGroup
            && consecutiveGroups >= maxConsecutiveGroups
            )
            )
      )

      if (lastGroup !== tokenGroup) {
        consecutiveGroups = 0
        lastGroup = tokenGroup
      }
      consecutiveGroups++

      val groupTokens = tokenGroup.tokens
      builder.append(groupTokens[RANDOM.nextInt(groupTokens.size)])
    }
    return builder.toString()
  }

  companion object {
    var PARAMETERS_TYPE: Type = object: TypeToken<MixedGroupParameters?>() {}.type
    var GSON: Gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
    var RANDOM: Random = Random()
  }
}
