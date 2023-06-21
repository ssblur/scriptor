package com.ssblur.scriptor.blockentity.renderers;

import com.mojang.blaze3d.vertex.*;
import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.blockentity.RuneBlockEntity;
import com.ssblur.scriptor.color.CustomColors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RuneBlockEntityRenderer implements BlockEntityRenderer<RuneBlockEntity> {
  static ResourceLocation magicCircle = new ResourceLocation(ScriptorMod.MOD_ID, "textures/entity/magic_circle.png");
  static ResourceLocation emptyCircle = new ResourceLocation(ScriptorMod.MOD_ID, "textures/entity/empty_circle.png");
  static RenderType magicLayer =
    RenderType
      .create(
        ScriptorMod.MOD_ID + ":rune",
        DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
        VertexFormat.Mode.QUADS,
        64,
        false,
        false,
        RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(magicCircle, false, false))
          .setCullState(new RenderStateShard.CullStateShard(false))
          .setLightmapState(new RenderStateShard.LightmapStateShard(true))
          .setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER)
          .createCompositeState(true)
      );
  static RenderType emptyLayer =
    RenderType
      .create(
        ScriptorMod.MOD_ID + ":circle",
        DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
        VertexFormat.Mode.QUADS,
        64,
        false,
        false,
        RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(emptyCircle, false, false))
          .setCullState(new RenderStateShard.CullStateShard(false))
          .setLightmapState(new RenderStateShard.LightmapStateShard(true))
          .setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER)
          .createCompositeState(true)
      );

  public RuneBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

  }
  @Override
  public void render(RuneBlockEntity rune, float tickDelta, PoseStack matrix, MultiBufferSource buffers, int light, int j) {
    if(rune.getLevel() == null) return;
    matrix.pushPose();

    int c = CustomColors.getColor(rune.color, rune.getLevel().getGameTime() + tickDelta);
    int r, g, b;
    var mc = Minecraft.getInstance();
    assert mc.level != null;
    r = (c & 0xff0000) >> 16;
    g = (c & 0x00ff00) >> 8;
    b = c & 0x0000ff;
    float yo = 0.0625f;
    matrix.translate(0, yo, 0);
    var pose = matrix.last().pose();
    var buffer = buffers.getBuffer(magicLayer);

    int cycle = 240;
    double rot = rune.getLevel().getGameTime() % cycle;
    rot = ((rot + tickDelta) / cycle) * 2 * Math.PI;
    double R = 0.5;
    float x = (float) (R * Math.cos(rot));
    float y = (float) (R * Math.sin(rot));
    buffer.vertex(pose, x + 0.5f, 0, y + 0.5f).color(r, g, b, 255).uv(0, 1).uv2(0xF000F0).endVertex();
    rot += (Math.PI / 2);
    x = (float) (R * Math.cos(rot));
    y = (float) (R * Math.sin(rot));
    buffer.vertex(pose, x + 0.5f, 0, y + 0.5f).color(r, g, b, 255).uv(1, 1).uv2(0xF000F0).endVertex();
    rot += (Math.PI / 2);
    x = (float) (R * Math.cos(rot));
    y = (float) (R * Math.sin(rot));
    buffer.vertex(pose, x + 0.5f, 0, y + 0.5f).color(r, g, b, 255).uv(1, 0).uv2(0xF000F0).endVertex();
    rot += (Math.PI / 2);
    x = (float) (R * Math.cos(rot));
    y = (float) (R * Math.sin(rot));
    buffer.vertex(pose, x + 0.5f, 0, y + 0.5f).color(r, g, b, 255).uv(0, 0).uv2(0xF000F0).endVertex();

    buffer = buffers.getBuffer(emptyLayer);
    R = 0.2;
    rot *= -1;
    x = (float) (R * Math.cos(rot));
    y = (float) (R * Math.sin(rot));
    buffer.vertex(pose, x + 0.5f, 0, y + 0.5f).color(r, g, b, 255).uv(0, 1).uv2(0xF000F0).endVertex();
    rot += (Math.PI / 2);
    x = (float) (R * Math.cos(rot));
    y = (float) (R * Math.sin(rot));
    buffer.vertex(pose, x + 0.5f, 0, y + 0.5f).color(r, g, b, 255).uv(1, 1).uv2(0xF000F0).endVertex();
    rot += (Math.PI / 2);
    x = (float) (R * Math.cos(rot));
    y = (float) (R * Math.sin(rot));
    buffer.vertex(pose, x + 0.5f, 0, y + 0.5f).color(r, g, b, 255).uv(1, 0).uv2(0xF000F0).endVertex();
    rot += (Math.PI / 2);
    x = (float) (R * Math.cos(rot));
    y = (float) (R * Math.sin(rot));
    buffer.vertex(pose, x + 0.5f, 0, y + 0.5f).color(r, g, b, 255).uv(0, 0).uv2(0xF000F0).endVertex();

    matrix.popPose();
  }
}
