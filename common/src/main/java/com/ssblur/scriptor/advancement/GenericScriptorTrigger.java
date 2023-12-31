package com.ssblur.scriptor.advancement;


import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ssblur.scriptor.ScriptorMod;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public class GenericScriptorTrigger extends SimpleCriterionTrigger<GenericScriptorTrigger.Instance> {

  ResourceLocation location;

  public GenericScriptorTrigger(ResourceLocation location) {
    this.location = location;
  }

  public void trigger(ServerPlayer player) {
    try {
      this.trigger(player, (instance) -> true);
    } catch(NullPointerException e) {
      ScriptorMod.LOGGER.error("An error occurred while trying to award an advancement:");
      ScriptorMod.LOGGER.error(e);
    }
  }

  @Override
  public Codec<Instance> codec() {
    return Instance.CODEC;
  }


  public static class Instance implements SimpleInstance {
    static Codec<Instance> CODEC = RecordCodecBuilder.create(
      (instance) ->
        instance
          .group(ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(Instance::player))
          .apply(instance, Instance::new)
    );
    ContextAwarePredicate player;
    public Instance(ContextAwarePredicate predicate) {
      this.player = predicate;
    }

    public Instance(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<ContextAwarePredicate> predicate) {
      this.player = predicate.orElse(null);
    }

    public Instance() {
      this.player = null;
    }

    @Override
    public Optional<ContextAwarePredicate> player() {
      return Optional.ofNullable(player);
    }
  }
}