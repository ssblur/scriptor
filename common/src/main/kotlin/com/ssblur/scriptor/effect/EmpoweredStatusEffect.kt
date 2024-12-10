package com.ssblur.scriptor.effect

import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity

class EmpoweredStatusEffect(var scale: Float) :
    MobEffect(MobEffectCategory.BENEFICIAL, 0x565656) {
    override fun applyEffectTick(entity: LivingEntity, amplifier: Int): Boolean {
        return true
    }
}