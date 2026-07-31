package com.ssblur.scriptor.mixin;

import com.ssblur.scriptor.block.ScriptorBlocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "updateSwimming", at = @At("TAIL"))
    private void scriptor$updateSwimming(CallbackInfo ci) {
        var self = (Entity) (Object) this;
        if(self.level().getBlockStates(self.getBoundingBox()).limit(4).anyMatch(state ->
                state.is(ScriptorBlocks.INSTANCE.getPHASED_BLOCK().get())
        )) self.setSwimming(self.isSprinting() && !self.isPassenger());
    }

    @Inject(method = "travel", at = @At("TAIL"))
    private void scriptor$travel(Vec3 vec3, CallbackInfo ci) {
        var self = (Player) (Object) this;
        var jumping = (LivingEntityAccessor) self;
        if(self.level().getBlockStates(self.getBoundingBox()).limit(4).noneMatch(state ->
            state.is(ScriptorBlocks.INSTANCE.getPHASED_BLOCK().get())
        )) return;

        if (jumping.isJumping()) {
            var vec = self.getDeltaMovement();
            self.setDeltaMovement(vec.add(0.0d, 0.025d, 0.0d));
        }
    }
}
