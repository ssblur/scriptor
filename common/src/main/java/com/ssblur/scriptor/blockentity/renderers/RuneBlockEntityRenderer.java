package com.ssblur.scriptor.blockentity.renderers;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
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
  static ResourceLocation magicCircle = ScriptorMod.location("textures/entity/magic_circle.png");
  static ResourceLocation emptyCircle = ScriptorMod.location("textures/entity/empty_circle.png");
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
          .setShaderState(RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
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
          .setShaderState(RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
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
    buffer.addVertex(pose, x + 0.5f, 0, y + 0.5f).setColor(r, g, b, 255).setUv(0, 1).setLight(0xF000F0);
    rot += (Math.PI / 2);
    x = (float) (R * Math.cos(rot));
    y = (float) (R * Math.sin(rot));
    buffer.addVertex(pose, x + 0.5f, 0, y + 0.5f).setColor(r, g, b, 255).setUv(1, 1).setLight(0xF000F0);
    rot += (Math.PI / 2);
    x = (float) (R * Math.cos(rot));
    y = (float) (R * Math.sin(rot));
    buffer.addVertex(pose, x + 0.5f, 0, y + 0.5f).setColor(r, g, b, 255).setUv(1, 0).setLight(0xF000F0);
    rot += (Math.PI / 2);
    x = (float) (R * Math.cos(rot));
    y = (float) (R * Math.sin(rot));
    buffer.addVertex(pose, x + 0.5f, 0, y + 0.5f).setColor(r, g, b, 255).setUv(0, 0).setLight(0xF000F0);

    buffer = buffers.getBuffer(emptyLayer);
    R = 0.2;
    rot *= -1;
    x = (float) (R * Math.cos(rot));
    y = (float) (R * Math.sin(rot));
    buffer.addVertex(pose, x + 0.5f, 0, y + 0.5f).setColor(r, g, b, 255).setUv(0, 1).setLight(0xF000F0);
    rot += (Math.PI / 2);
    x = (float) (R * Math.cos(rot));
    y = (float) (R * Math.sin(rot));
    buffer.addVertex(pose, x + 0.5f, 0, y + 0.5f).setColor(r, g, b, 255).setUv(1, 1).setLight(0xF000F0);
    rot += (Math.PI / 2);
    x = (float) (R * Math.cos(rot));
    y = (float) (R * Math.sin(rot));
    buffer.addVertex(pose, x + 0.5f, 0, y + 0.5f).setColor(r, g, b, 255).setUv(1, 0).setLight(0xF000F0);
    rot += (Math.PI / 2);
    x = (float) (R * Math.cos(rot));
    y = (float) (R * Math.sin(rot));
    buffer.addVertex(pose, x + 0.5f, 0, y + 0.5f).setColor(r, g, b, 255).setUv(0, 0).setLight(0xF000F0);

    matrix.popPose();
  }
}
