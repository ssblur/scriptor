package com.ssblur.scriptor.events;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class ScriptorEventsExpectPlatform {
  @ExpectPlatform
  public static void registerClientEvents() {
    throw new AssertionError();
  }
}
