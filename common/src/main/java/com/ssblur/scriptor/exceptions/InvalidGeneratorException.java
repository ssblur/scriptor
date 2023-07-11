package com.ssblur.scriptor.exceptions;

import net.minecraft.resources.ResourceLocation;

public class InvalidGeneratorException extends RuntimeException {
  public InvalidGeneratorException(String generator, ResourceLocation location) {
    super(String.format("Generator '%s' does not exist or is not registered! (Used by %s)", generator, location));
  }
}
