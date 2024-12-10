package com.ssblur.scriptor.feature

import com.mojang.serialization.Codec
import com.ssblur.scriptor.block.GenerateBlock
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration

class ScriptorInscriptionsFeature(codec: Codec<NoneFeatureConfiguration?>?) :
    Feature<NoneFeatureConfiguration?>(codec) {
    override fun place(context: FeaturePlaceContext<NoneFeatureConfiguration?>): Boolean {
        val level = context.level()
        val pos = context.origin()

        level.setBlock(pos, GenerateBlock.generateEngraving(), 0)

        return true
    }
}
