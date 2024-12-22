package com.ssblur.scriptor.resources

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.helpers.resource.EngravingResource
import com.ssblur.unfocused.data.DataLoaderRegistry.registerSimpleDataLoader

object Engravings {
    val engravings = ScriptorMod.registerSimpleDataLoader("scriptor/engravings", EngravingResource::class)
    fun getRandomEngraving() = engravings.entries.random().value
}