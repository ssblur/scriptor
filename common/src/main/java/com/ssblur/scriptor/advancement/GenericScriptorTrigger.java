package com.ssblur.scriptor.advancement;


import com.google.gson.JsonObject;
import com.ssblur.scriptor.ScriptorMod;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;

import java.util.Optional;

public class GenericScriptorTrigger extends SimpleCriterionTrigger<GenericScriptorTrigger.Instance> {

  ResourceLocation location;

  public GenericScriptorTrigger(ResourceLocation location) {
    this.location = location;
  }

  public void trigger(ServerPlayer player) {
    // TODO: simpleInstance is null producing errors when awarding advancements?
    try {
      this.trigger(player, (instance) -> true);
    } catch(NullPointerException e) {
      ScriptorMod.LOGGER.error("An error occurred while trying to award an advancement:");
      ScriptorMod.LOGGER.error(e);
    }
  }

  @Override
  protected Instance createInstance(JsonObject jsonObject, Optional<ContextAwarePredicate> optional, DeserializationContext deserializationContext) {
    return optional.map(Instance::new).orElse(new Instance());
  }


  public static class Instance extends AbstractCriterionTriggerInstance {
    public Instance(ContextAwarePredicate predicate) {
      super(Optional.of(predicate));
    }

    public Instance() {
      super(Optional.empty());
    }
  }
}