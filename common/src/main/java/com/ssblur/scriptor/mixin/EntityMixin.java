package com.ssblur.scriptor.mixin;

import com.ssblur.scriptor.block.ScriptorBlocks;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "updateInWaterStateAndDoWaterCurrentPushing", at = @At("RETURN"))
    private void scriptor$updateInWaterStateAndDoWaterCurrentPushing(CallbackInfo ci) {
        var self = (Entity) (Object) this;
        var accessor = (EntityAccessor) self;
        if(accessor.getWasTouchingWater()) return;
        if(self.level().getBlockStates(self.getBoundingBox()).anyMatch(state ->
            state.is(ScriptorBlocks.INSTANCE.getPHASED_BLOCK().get())
        )) accessor.setWasTouchingWater(true);
    }
}
