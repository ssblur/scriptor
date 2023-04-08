package com.ssblur.scriptor.damage;

import net.minecraft.world.damagesource.DamageSource;

public class SacrificeDamageSource extends DamageSource {
  public SacrificeDamageSource() {
    super("sacrifice");
  }

  @Override
  public boolean isBypassArmor() {
    return true;
  }
}
