package com.ssblur.scriptor.feature;

import com.ssblur.scriptor.ScriptorMod;
import dev.architectury.registry.level.biome.BiomeModifications;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class ScriptorFeatures {
  public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ScriptorMod.MOD_ID, Registries.FEATURE);

  public static final RegistrySupplier<Feature<?>> ENGRAVING_FEATURE = FEATURES.register("engraving",
    () -> new ScriptorInscriptionsFeature(NoneFeatureConfiguration.CODEC)
  );

  public static void register() {
    FEATURES.register();
    BiomeModifications.addProperties(
      biome -> biome.hasTag(BiomeTags.IS_OVERWORLD),
      (biomeContext, mutable) ->
        mutable.getGenerationProperties().addFeature(
          GenerationStep.Decoration.RAW_GENERATION,
          ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.tryBuild("scriptor", "engravings"))
    ));
  }
}
