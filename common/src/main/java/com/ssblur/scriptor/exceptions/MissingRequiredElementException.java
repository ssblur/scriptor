package com.ssblur.scriptor.exceptions;

import net.minecraft.resources.ResourceLocation;

public class MissingRequiredElementException extends RuntimeException {
  public MissingRequiredElementException(String element, ResourceLocation location) {
    super(String.format("Missing required element %s when attempting to load %s", element, location));
  }

  public MissingRequiredElementException(String element, String message) {
    super(String.format("Missing required element %s: %s", element, message));
  }
}
