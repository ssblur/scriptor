package com.ssblur.scriptor.advancement;


import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class GenericScriptorTrigger extends SimpleCriterionTrigger<GenericScriptorTrigger.Instance> {

  ResourceLocation location;

  public GenericScriptorTrigger(ResourceLocation location) {
    this.location = location;
  }

  @Override
  public ResourceLocation getId() {
    return location;
  }

  @Override
  protected GenericScriptorTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext ctx) {
    return new GenericScriptorTrigger.Instance(player);
  }

  public void trigger(ServerPlayer player) {
    this.trigger(player, (instance) -> true);
  }


  public class Instance extends AbstractCriterionTriggerInstance {
    public Instance(ContextAwarePredicate player) {
      super(location, player);
    }
  }

  public Instance collectTome() {
    return new Instance(ContextAwarePredicate.ANY);
  }
}