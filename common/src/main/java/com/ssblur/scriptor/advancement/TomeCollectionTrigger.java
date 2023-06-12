package com.ssblur.scriptor.advancement;


import com.google.gson.JsonObject;
import com.ssblur.scriptor.ScriptorMod;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class TomeCollectionTrigger extends SimpleCriterionTrigger<TomeCollectionTrigger.Instance> {

  ResourceLocation location;

  public TomeCollectionTrigger(ResourceLocation location) {
    this.location = location;
  }

  @Override
  public ResourceLocation getId() {
    return location;
  }

  @Override
  protected TomeCollectionTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext ctx) {
    return new TomeCollectionTrigger.Instance(player);
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