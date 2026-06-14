package com.ssblur.scriptor.data.components

import com.ssblur.unfocused.serialization.KClassCodec
import net.minecraft.resources.ResourceLocation

data class ReagentData(val items: Map<ResourceLocation, Int>) {
  companion object {
    val CODEC = KClassCodec.codec(ReagentData::class)
    val STREAM_CODEC = KClassCodec.streamCodec(ReagentData::class)

    val NONE = ReagentData(mapOf())
  }
}