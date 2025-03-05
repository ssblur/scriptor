package com.ssblur.scriptor.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.ssblur.scriptor.item.interfaces.ItemWithCustomRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(LevelRenderer.class)
public class ScriptorLevelRendererMixin {
    @Inject(
            method = "renderLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/ClientLevel;entitiesForRendering()Ljava/lang/Iterable;",
                    shift = At.Shift.AFTER
            )
    )
    public void scriptor$renderLevel(
            DeltaTracker deltaTracker,
            boolean bl,
            Camera camera,
            GameRenderer gameRenderer,
            LightTexture lightTexture,
            Matrix4f matrix4f,
            Matrix4f matrix4f2,
            CallbackInfo ci,
            @Local PoseStack poseStack,
            @Local MultiBufferSource.BufferSource bufferSource
    ) {
        var localPlayer = Minecraft.getInstance().player;
        if(localPlayer == null) return;
        var mainHand = localPlayer.getItemInHand(InteractionHand.MAIN_HAND);
        if(mainHand.getItem() instanceof ItemWithCustomRenderer renderable)
            renderable.worldRender(localPlayer, InteractionHand.MAIN_HAND, mainHand, poseStack, bufferSource);
        var offHand = localPlayer.getItemInHand(InteractionHand.OFF_HAND);
        if(offHand.getItem() instanceof ItemWithCustomRenderer renderable)
            renderable.worldRender(localPlayer, InteractionHand.OFF_HAND, offHand, poseStack, bufferSource);
    }
}
