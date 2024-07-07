package com.ssblur.scriptor.registry.words;

import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.word.descriptor.target.OffsetDescriptor;

@SuppressWarnings("unused")
public class OffsetDescriptors {
  private static final WordRegistry INSTANCE = WordRegistry.INSTANCE;

  public final Descriptor MOVE_RIGHT = INSTANCE.register("move_right", new OffsetDescriptor().right());
  public final Descriptor COPY_RIGHT = INSTANCE.register("copy_right", new OffsetDescriptor().duplicate().right());
  public final Descriptor MOVE_LEFT = INSTANCE.register("move_left", new OffsetDescriptor().left());
  public final Descriptor COPY_LEFT = INSTANCE.register("copy_left", new OffsetDescriptor().duplicate().left());
  public final Descriptor MOVE_FORWARDS = INSTANCE.register("move_forwards", new OffsetDescriptor().forward());
  public final Descriptor COPY_FORWARDS = INSTANCE.register("copy_forwards", new OffsetDescriptor().duplicate().forward());
  public final Descriptor MOVE_BACKWARDS = INSTANCE.register("move_backwards", new OffsetDescriptor().backwards());
  public final Descriptor COPY_BACKWARDS = INSTANCE.register("copy_backwards", new OffsetDescriptor().duplicate().backwards());
  public final Descriptor MOVE_UP = INSTANCE.register("move_up", new OffsetDescriptor().up());
  public final Descriptor COPY_UP = INSTANCE.register("copy_up", new OffsetDescriptor().duplicate().up());
  public final Descriptor MOVE_DOWN = INSTANCE.register("move_down", new OffsetDescriptor().down());
  public final Descriptor COPY_DOWN = INSTANCE.register("copy_down", new OffsetDescriptor().duplicate().down());
}
