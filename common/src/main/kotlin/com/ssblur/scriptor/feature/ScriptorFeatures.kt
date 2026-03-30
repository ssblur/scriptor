package com.ssblur.scriptor.feature

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.block.GenerateBlock
import com.ssblur.scriptor.block.ScriptorBlocks
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration

@Suppress("unused")
object ScriptorFeatures {
  val ENGRAVING_FEATURE = ScriptorMod.registerFeature("engraving") {
    object: Feature<NoneFeatureConfiguration?>(NoneFeatureConfiguration.CODEC) {
      override fun place(context: FeaturePlaceContext<NoneFeatureConfiguration?>): Boolean {
        if(context.random().nextBoolean())
          return context.level().setBlock(context.origin(), GenerateBlock.generateEngraving(), 0)

        val ore = ScriptorBlocks.ORE.first.get().defaultBlockState()
        context.level().setBlock(context.origin(), ore, 0)
        var loc = context.origin()
        for(i in 0..context.random().nextInt(2, 15)) {
          loc = if(context.random().nextBoolean()) loc.offset(0, -1, 0)
            else loc.offset(context.random().nextInt(-1, 1), -1, context.random().nextInt(-1, 1))
          context.level().setBlock(loc, ore, 0)
        }

        return true
      }
    }
  }

  fun register() {}
}
