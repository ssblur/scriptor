package com.ssblur.scriptor.mixin;

import com.ssblur.scriptor.item.ScriptorItems;
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
public class LecternBlockEntitySpellbookMixin {
  @Inject(method = "hasBook", at = @At("HEAD"), cancellable = true)
  public void hasBook(CallbackInfoReturnable<Boolean> info) {
    if(((LecternBlockEntity) (Object) this).getBook().is(ScriptorItems.SPELLBOOK.get())) {
      info.setReturnValue(true);
    }
  }

  @Inject(method = "resolveBook", at = @At("HEAD"))
  private void resolveBook(ItemStack itemStack, @Nullable Player player, CallbackInfoReturnable<ItemStack> info) {
    var self = ((LecternBlockEntity) (Object) this);
    if (self.getLevel() instanceof ServerLevel && itemStack.is(ScriptorItems.SPELLBOOK.get())) {
      WrittenBookItem.resolveBookComponents(itemStack, self.createCommandSourceStack(player), player);
    }
  }
}
