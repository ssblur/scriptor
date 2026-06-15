package com.ssblur.scriptor.data.components

import com.ssblur.unfocused.serialization.KClassCodec

data class ConditionData(val conditions: List<String>) {
  companion object {
    val CODEC = KClassCodec.codec(ConditionData::class)
    val STREAM_CODEC = KClassCodec.streamCodec(ConditionData::class)

    val NONE = ConditionData(listOf())
  }
}