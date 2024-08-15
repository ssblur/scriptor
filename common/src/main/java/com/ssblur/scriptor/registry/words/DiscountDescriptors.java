package com.ssblur.scriptor.registry.words;

import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.word.descriptor.discount.*;

@SuppressWarnings("unused")
public class DiscountDescriptors {
  private static final WordRegistry INSTANCE = WordRegistry.INSTANCE;

  public final Descriptor BLOOD_COST = INSTANCE.register("blood_cost", new BloodCostDescriptor());
  public final Descriptor CHEAP = INSTANCE.register("cheap", new CheapDescriptor());
  public final Descriptor HEALTHY = INSTANCE.register("healthy", new HealthyDescriptor());
  public final Descriptor POISONED = INSTANCE.register("poisoned", new PoisonDescriptor());
  public final Descriptor WEAKENED = INSTANCE.register("weakened", new WeakDescriptor());
  public final Descriptor CRITICAL = INSTANCE.register("critical", new CriticalDescriptor());
  public final Descriptor NIGHT = INSTANCE.register("night", new NightDiscountDescriptor());
  public final Descriptor RAIN = INSTANCE.register("rain", new RainDiscountDescriptor());
}
