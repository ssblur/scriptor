package com.ssblur.scriptor.mixin;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.helpers.SpellbookAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.gui.screens.inventory.LecternScreen;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(BookViewScreen.class)
public class BookViewScreenSpellbookMixin {
  private static final ResourceLocation BOOK_LOCATION_NEW = new ResourceLocation(ScriptorMod.MOD_ID, "textures/gui/book.png");
  private static final ResourceLocation BOOK_LOCATION_DISABLED = new ResourceLocation(ScriptorMod.MOD_ID, "textures/gui/book_disabled.png");

  @Inject(
    method = "renderBackground",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V",
      shift = At.Shift.AFTER
    )
  )
  public void renderBackground(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo info) {
    var self = (BookViewScreen) (Object) this;
    int k = (self.width - 192) / 2;
    if(self.bookAccess instanceof SpellbookAccess)
      guiGraphics.blit(BOOK_LOCATION_NEW, k, 2, 0, 0, 192, 192);
    else if(self.bookAccess == null)
      guiGraphics.blit(BOOK_LOCATION_DISABLED, k, 2, 0, 0, 192, 192);
  }
}

