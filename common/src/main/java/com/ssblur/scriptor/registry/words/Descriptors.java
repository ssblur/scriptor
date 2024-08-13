package com.ssblur.scriptor.registry.words;

import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.word.descriptor.SpeedDurationDescriptor;
import com.ssblur.scriptor.word.descriptor.discount.*;
import com.ssblur.scriptor.word.descriptor.duration.SimpleDurationDescriptor;
import com.ssblur.scriptor.word.descriptor.power.BloodPowerDescriptor;
import com.ssblur.scriptor.word.descriptor.power.OverwhelmingStrengthDescriptor;
import com.ssblur.scriptor.word.descriptor.power.SimpleStrengthDescriptor;
import com.ssblur.scriptor.word.descriptor.target.ChainDescriptor;
import com.ssblur.scriptor.word.descriptor.target.NetherDescriptor;

@SuppressWarnings("unused")
public class Descriptors {
  private static final WordRegistry INSTANCE = WordRegistry.INSTANCE;

  public final Descriptor LONG = INSTANCE.register("long", new SimpleDurationDescriptor(3, 7));
  public final Descriptor LONGER = INSTANCE.register("longer", new SimpleDurationDescriptor(6, 17));
  public final Descriptor VERY_LONG = INSTANCE.register("very_long", new SimpleDurationDescriptor(65, 120));
  public final Descriptor STACKING_LONG = INSTANCE.register("stacking_long", new SimpleDurationDescriptor(6, 7).allowDuplication());
  public final Descriptor SLOW = INSTANCE.register("slow", new SpeedDurationDescriptor(2, 4, 0.75));
  public final Descriptor FAST = INSTANCE.register("fast", new SpeedDurationDescriptor(2, -4, 1.25));
  public final Descriptor CHAIN = INSTANCE.register("chain", new ChainDescriptor());

  public final Descriptor BLOOD_POWER = INSTANCE.register("blood_power", new BloodPowerDescriptor());
  public final Descriptor STRONG = INSTANCE.register("strong", new SimpleStrengthDescriptor(2, 1));
  public final Descriptor POWERFUL = INSTANCE.register("powerful", new SimpleStrengthDescriptor(6, 4));
  public final Descriptor STACKING_STRONG = INSTANCE.register("stacking_strong", new SimpleStrengthDescriptor(4, 1).allowDuplication());
  public final Descriptor MASSIVE_STRONG = INSTANCE.register("massive_strong", new SimpleStrengthDescriptor(120, 30));
  public final Descriptor OVERWHELMING_STRENGTH = INSTANCE.register("overwhelming", new OverwhelmingStrengthDescriptor());

  public final Descriptor BLOOD_COST = INSTANCE.register("blood_cost", new BloodCostDescriptor());
  public final Descriptor CHEAP = INSTANCE.register("cheap", new CheapDescriptor());
  public final Descriptor HEALTHY = INSTANCE.register("healthy", new HealthyDescriptor());
  public final Descriptor POISONED = INSTANCE.register("poisoned", new PoisonDescriptor());
  public final Descriptor CRITICAL = INSTANCE.register("critical", new CriticalDescriptor());

  public final Descriptor NETHER = INSTANCE.register("nether", new NetherDescriptor());
}
