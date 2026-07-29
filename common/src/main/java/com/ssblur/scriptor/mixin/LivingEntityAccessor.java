package com.ssblur.scriptor.mixin;

import org.spongepowered.asm.mixin.gen.Accessor;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.world.entity.LivingEntity.class)
public interface LivingEntityAccessor {
    @Accessor
    boolean isJumping();
}
