package com.ssblur.scriptor.resources

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.helpers.resource.ArtifactResource
import com.ssblur.unfocused.data.DataLoaderRegistry.registerSimpleDataLoader

object Artifacts {
    val artifacts = ScriptorMod.registerSimpleDataLoader("scriptor/artifacts", ArtifactResource::class)
    fun getRandomArtifact() = artifacts.entries.random().value
}