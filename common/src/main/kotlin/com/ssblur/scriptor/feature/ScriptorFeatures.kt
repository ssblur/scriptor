package com.ssblur.scriptor.feature

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.block.GenerateBlock
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration

@Suppress("unused")
object ScriptorFeatures {
    val ENGRAVING_FEATURE = ScriptorMod.registerFeature("engraving") {
        object: Feature<NoneFeatureConfiguration?>(NoneFeatureConfiguration.CODEC) {
            override fun place(context: FeaturePlaceContext<NoneFeatureConfiguration?>): Boolean {
                return context.level().setBlock(context.origin(), GenerateBlock.generateEngraving(), 0)
            }
        }
    }

    fun register() {}
}
