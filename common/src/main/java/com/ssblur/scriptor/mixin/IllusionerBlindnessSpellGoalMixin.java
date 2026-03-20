package com.ssblur.scriptor.mixin;

import com.ssblur.scriptor.helpers.FakeCastHelper;
import net.minecraft.world.entity.monster.Illusioner;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = { "net.minecraft.world.entity.monster.Illusioner$IllusionerBlindnessSpellGoal" })
public class IllusionerBlindnessSpellGoalMixin {
    @Shadow
    @Final
    Illusioner field_7299;

    @Inject(method = "performSpellCasting", at = @At("RETURN"))
    private void scriptor$performSpellCasting(CallbackInfo ci) {
        var entity = this.field_7299;
        FakeCastHelper.INSTANCE.castAs(entity, FakeCastHelper.INSTANCE.getILLUSIONER_BLINDNESS());
    }
}
