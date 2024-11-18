package com.ssblur.scriptor.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ssblur.scriptor.item.interfaces.ItemWithCustomRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ItemInHandRenderer.class)
public class ScriptorItemInHandRendererMixin {
  @Inject(method = "renderArmWithItem", at = @At("HEAD"), cancellable = true)
  public void scriptor$renderArmWithItem(
    AbstractClientPlayer player,
    float i,
    float pitch,
    InteractionHand hand,
    float swingProgress,
    ItemStack itemStack,
    float readyProgress,
    PoseStack matrix,
    MultiBufferSource buffer,
    int lightLevel,
    CallbackInfo info
  ) {
    if(itemStack.getItem() instanceof ItemWithCustomRenderer renderable)
      if(renderable.render(player, i, pitch, hand, swingProgress, itemStack, readyProgress, matrix, buffer, lightLevel)) info.cancel();
  }
}
