package com.ssblur.scriptor.registry.words;

import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.word.descriptor.power.BloodPowerDescriptor;
import com.ssblur.scriptor.word.descriptor.power.OverwhelmingStrengthDescriptor;
import com.ssblur.scriptor.word.descriptor.power.SimpleStrengthDescriptor;
import com.ssblur.scriptor.word.descriptor.power.SolarPowerDescriptor;

@SuppressWarnings("unused")
public class PowerDescriptors {
  private static final WordRegistry INSTANCE = WordRegistry.INSTANCE;

  public final Descriptor BLOOD_POWER = INSTANCE.register("blood_power", new BloodPowerDescriptor());
  public final Descriptor STRONG = INSTANCE.register("strong", new SimpleStrengthDescriptor(2, 1));
  public final Descriptor POWERFUL = INSTANCE.register("powerful", new SimpleStrengthDescriptor(6, 4));
  public final Descriptor STACKING_STRONG = INSTANCE.register("stacking_strong", new SimpleStrengthDescriptor(4, 1).allowDuplication());
  public final Descriptor MASSIVE_STRONG = INSTANCE.register("massive_strong", new SimpleStrengthDescriptor(120, 30));
  public final Descriptor OVERWHELMING_STRENGTH = INSTANCE.register("overwhelming", new OverwhelmingStrengthDescriptor());
  public final Descriptor SOLAR_POWER = INSTANCE.register("solar_power", new SolarPowerDescriptor());
}
