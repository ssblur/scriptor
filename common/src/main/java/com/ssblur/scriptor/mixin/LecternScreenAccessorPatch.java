package com.ssblur.scriptor.mixin;

import com.ssblur.scriptor.helpers.SpellbookAccess;
import com.ssblur.scriptor.item.ScriptorItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.inventory.LecternScreen;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LecternScreen.class)
public class LecternScreenAccessorPatch {
  @Inject(method = "bookChanged", at = @At("TAIL"))
  void bookChanged(CallbackInfo info) {
    var self = (LecternScreen) (Object) this;
    ItemStack itemStack = self.getMenu().getBook();
    if(itemStack.is(ScriptorItems.SPELLBOOK.get()))
      self.setBookAccess(new SpellbookAccess(itemStack));
  }
}
