package com.ssblur.scriptor.mixin;

import com.ssblur.scriptor.block.ScriptorBlocks;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "isInWater", cancellable = true, at = @At("RETURN"))
    private void scriptor$isInWater(CallbackInfoReturnable<Boolean> cir) {
        if(cir.getReturnValue() == true) return;
        var self = (Entity) (Object) this;
        if(self.level().getBlockStates(self.getBoundingBox()).anyMatch(state ->
            state.is(ScriptorBlocks.INSTANCE.getPHASED_BLOCK().get())
        )) cir.setReturnValue(true);
    }
}
