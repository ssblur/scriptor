package com.ssblur.scriptor.mixin;

import com.ssblur.scriptor.item.ScriptorTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LecternBlockEntity.class)
public class ScriptorLecternBlockEntityMixin {
  @Inject(method = "hasBook", at = @At("HEAD"), cancellable = true)
  public void scriptor$hasBook(CallbackInfoReturnable<Boolean> info) {
    var self = (LecternBlockEntity) (Object) this;
    if(self.getBook().is(ScriptorTags.READABLE_SPELLBOOKS)) {
      info.setReturnValue(true);
    }
  }

  @Inject(method = "resolveBook", at = @At("HEAD"))
  private void scriptor$resolveBook(ItemStack itemStack, @Nullable Player player, CallbackInfoReturnable<ItemStack> info) {
    var self = ((LecternBlockEntity) (Object) this);
    if (self.getLevel() instanceof ServerLevel && itemStack.is(ScriptorTags.READABLE_SPELLBOOKS)) {
      WrittenBookItem.resolveBookComponents(itemStack, self.createCommandSourceStack(player), player);
    }
  }
}
