package com.ssblur.scriptor.mixin;

import com.ssblur.scriptor.events.ItemRightClicked;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void scriptor$use(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        var self = (ItemStack) (Object) this;
        if(ItemRightClicked.INSTANCE.itemRightClicked(player, self)) cir.setReturnValue(InteractionResultHolder.success(self));
    }
}
