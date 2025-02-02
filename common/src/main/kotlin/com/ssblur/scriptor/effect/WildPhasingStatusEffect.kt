package com.ssblur.scriptor.effect

import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3

class WildPhasingStatusEffect: PhasingStatusEffect(MobEffectCategory.HARMFUL, 0x50007f) {
  override fun applyEffectTick(entity: LivingEntity, amplifier: Int): Boolean {
    if (entity is Player) phase(entity, -2)
    else entity.setPos(entity.position().add(Vec3(0.0, -0.05, 0.0)))
    return true
  }
}