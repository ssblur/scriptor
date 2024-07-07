package com.ssblur.scriptor.feature;

import com.mojang.serialization.Codec;
import com.ssblur.scriptor.block.GenerateBlock;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class ScriptorInscriptionsFeature extends Feature<NoneFeatureConfiguration> {
  public ScriptorInscriptionsFeature(Codec<NoneFeatureConfiguration> codec) {
    super(codec);
  }

  @Override
  public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
    var level = context.level();
    var pos = context.origin();

    level.setBlock(pos, GenerateBlock.generateEngraving(), 0);

    return true;
  }
}
