package com.ssblur.scriptor.mixin;

import com.google.common.collect.Iterables;
import com.ssblur.scriptor.recipe.GrindstoneRecipeHandler;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(GrindstoneMenu.class)
public class GrindstoneMenuMixin {
    @Inject(method = "computeResult", at = @At("RETURN"), cancellable = true)
    private void scriptor$computeResult(ItemStack itemStack, ItemStack itemStack2, CallbackInfoReturnable<ItemStack> cir) {
        var item = Iterables.tryFind(
                Arrays.asList(cir.getReturnValue(), itemStack, itemStack2),
                v -> v!= null && !v.isEmpty()
        );
        if(!item.isPresent()) return;
        var output = GrindstoneRecipeHandler.INSTANCE.handle(item.get());
        if(output != null) cir.setReturnValue(output);
    }
}
