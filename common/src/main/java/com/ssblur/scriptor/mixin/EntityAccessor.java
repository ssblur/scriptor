package com.ssblur.scriptor.mixin;

import org.spongepowered.asm.mixin.gen.Accessor;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.world.entity.Entity.class)
public interface EntityAccessor {
    @Accessor
    boolean getWasTouchingWater();

    @Accessor
    void setWasTouchingWater(boolean wasTouchingWater);
}
