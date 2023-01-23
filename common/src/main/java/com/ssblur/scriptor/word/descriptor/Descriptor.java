package com.ssblur.scriptor.word.descriptor;

public abstract class Descriptor {
  /**
   * @return A number representing material cost or cast cooldown.
   * Generally, for a Descriptor, anything that affects the spell should cost more than 0.
   * Purely aesthetic Descriptors should cost 0.
   * Some Descriptors may cost less than 0 if they introduce considerable downsides.
   * Whether these deduct from spell cost is up to the discretion of the developer of this
   * descriptor.
   */
  public abstract int cost();
}
