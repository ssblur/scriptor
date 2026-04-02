package com.ssblur.scriptor.feature

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.block.GenerateBlock
import com.ssblur.unfocused.extension.BlockStateExtension.matches
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration

@Suppress("unused")
object ScriptorFeatures {
  val ENGRAVING_FEATURE = ScriptorMod.registerFeature("engraving") {
    object: Feature<NoneFeatureConfiguration?>(NoneFeatureConfiguration.CODEC) {
      override fun place(context: FeaturePlaceContext<NoneFeatureConfiguration?>): Boolean {
        if (context.level().getBlockState(context.origin().offset(0, -1, 0)) matches Blocks.WATER)
          return false
        return context.level().setBlock(context.origin(), GenerateBlock.generateEngraving(), 0)
      }
    }
  }

  fun register() {}
}
