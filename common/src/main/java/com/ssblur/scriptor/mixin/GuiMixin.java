package com.ssblur.scriptor.mixin;

import com.ssblur.scriptor.events.ScriptorCooldownHud;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(method = "isExperienceBarVisible", at = @At("RETURN"), cancellable = true)
    private void scriptor$isExperienceBarVisible(CallbackInfoReturnable<Boolean> cir){
        if(ScriptorCooldownHud.INSTANCE.shouldNotRenderXpBar()) cir.setReturnValue(false);
    }
}
