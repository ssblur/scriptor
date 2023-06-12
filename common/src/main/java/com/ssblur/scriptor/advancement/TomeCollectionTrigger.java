package com.ssblur.scriptor.advancement;


import com.google.gson.JsonObject;
import com.ssblur.scriptor.ScriptorMod;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
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
  protected TomeCollectionTrigger.Instance createInstance(JsonObject json, EntityPredicate.Composite player, DeserializationContext ctx) {
    return new TomeCollectionTrigger.Instance(player);
  }

  public void trigger(ServerPlayer player) {
    this.trigger(player, (instance) -> true);
  }


  public class Instance extends AbstractCriterionTriggerInstance {
    public Instance(EntityPredicate.Composite player) {
      super(location, player);
    }
  }

  public Instance collectTome() {
    return new Instance(EntityPredicate.Composite.ANY);
  }
}