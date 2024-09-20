package com.ssblur.scriptor.registry.words;

import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.word.descriptor.SpeedDurationDescriptor;
import com.ssblur.scriptor.word.descriptor.duration.SimpleDurationDescriptor;
import com.ssblur.scriptor.api.word.descriptor.RepeatDescriptor;
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

  public final Descriptor NETHER = INSTANCE.register("nether", new NetherDescriptor());

  public final Descriptor REPEAT_2 = INSTANCE.register("repeat_2", new RepeatDescriptor(2));
  public final Descriptor REPEAT_3 = INSTANCE.register("repeat_3", new RepeatDescriptor(3));
  public final Descriptor REPEAT_5 = INSTANCE.register("repeat_5", new RepeatDescriptor(5));
  public final Descriptor REPEAT_7 = INSTANCE.register("repeat_7", new RepeatDescriptor(7));
}
