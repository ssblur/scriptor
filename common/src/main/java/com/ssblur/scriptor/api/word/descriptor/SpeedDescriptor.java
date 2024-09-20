package com.ssblur.scriptor.api.word.descriptor;

public interface SpeedDescriptor {
  /**
   * How much this descriptor will modify spell speed.
   * Currently only applies to projectiles.
   * @return A scalar to be multiplied with spell speed
   */
  double speedModifier();
}
