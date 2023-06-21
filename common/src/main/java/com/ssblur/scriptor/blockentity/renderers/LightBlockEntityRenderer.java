package com.ssblur.scriptor.blockentity.renderers;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.blockentity.LightBlockEntity;
import com.ssblur.scriptor.blockentity.RuneBlockEntity;
import com.ssblur.scriptor.helpers.CustomColors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.ResourceLocation;
import org.joml.Random;
import org.joml.Vector3f;

import java.awt.*;

public class LightBlockEntityRenderer implements BlockEntityRenderer<LightBlockEntity> {
  public LightBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

  @Override
  public void render(LightBlockEntity light, float tickDelta, PoseStack matrix, MultiBufferSource buffers, int lightLevel, int j) {
    if(light.getLevel() == null) return;

    int c = CustomColors.getColor(light.getColor(), light.getLevel().getGameTime() + tickDelta);
    float r = ((float) ((c & 0xff0000) >> 16)) / 255;
    float g = ((float) ((c & 0x00ff00) >> 8)) / 255;
    float b = ((float) (c & 0x0000ff)) / 255;

    var random = new Random();
    if(random.nextFloat() < 0.9f) return;
    float xd = 0.25f + random.nextFloat() / 2f;
    float yd = 0.25f + random.nextFloat() / 2f;
    float zd = 0.25f + random.nextFloat() / 2f;

    if(!Minecraft.getInstance().isPaused())
      light.getLevel().addParticle(
        new DustParticleOptions(new Vector3f(r, g, b), 1.0f),
        light.getBlockPos().getX() + xd,
        light.getBlockPos().getY() + yd,
        light.getBlockPos().getZ() + zd,
        0,
        0,
        0
      );
  }
}
