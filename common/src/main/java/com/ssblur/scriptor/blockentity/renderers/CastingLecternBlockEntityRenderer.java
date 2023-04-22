package com.ssblur.scriptor.blockentity.renderers;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.blockentity.CastingLecternBlockEntity;
import com.ssblur.scriptor.blockentity.RuneBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class CastingLecternBlockEntityRenderer implements BlockEntityRenderer<CastingLecternBlockEntity> {
  public CastingLecternBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

  }
  @Override
  public void render(CastingLecternBlockEntity lectern, float tickDelta, PoseStack matrix, MultiBufferSource buffers, int light, int j) {
    var itemRenderer = Minecraft.getInstance().getItemRenderer();
    itemRenderer.renderStatic(lectern.getSpellbook(), ItemTransforms.TransformType.GROUND, light, OverlayTexture.NO_OVERLAY, matrix, buffers, 0);
  }
}
