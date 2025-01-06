package com.ssblur.scriptor.feature

import com.ssblur.scriptor.ScriptorMod
import dev.architectury.registry.level.biome.BiomeModifications
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BiomeTags
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration

@Suppress("unused")
object ScriptorFeatures {
    val ENGRAVING_FEATURE = ScriptorMod.registerFeature("engraving") {
        ScriptorInscriptionsFeature(NoneFeatureConfiguration.CODEC)
    }

    fun register() {
        BiomeModifications.addProperties(
            { biome -> biome.hasTag(BiomeTags.IS_OVERWORLD) },
            { _, mutable ->
                mutable.generationProperties.addFeature(
                    GenerationStep.Decoration.RAW_GENERATION,
                    ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath("scriptor", "engravings"))
                )
            })
    }
}
