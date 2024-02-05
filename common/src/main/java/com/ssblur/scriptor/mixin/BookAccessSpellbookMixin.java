package com.ssblur.scriptor.mixin;

import com.ssblur.scriptor.helpers.SpellbookAccess;
import com.ssblur.scriptor.item.ScriptorItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(BookViewScreen.BookAccess.class)
public interface BookAccessSpellbookMixin {

  @Inject(method = "fromItem", at = @At("HEAD"), cancellable = true)
  private static void fromItem(ItemStack itemStack, CallbackInfoReturnable<BookViewScreen.BookAccess> info) {
    if (itemStack.is(ScriptorItems.SPELLBOOK.get())) {
      info.setReturnValue(new SpellbookAccess(itemStack));
      info.cancel();
    }
  }
}