package com.ssblur.scriptor.blockentity.renderers;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.block.ScriptorBlocks;
import com.ssblur.scriptor.blockentity.ChalkBlockEntity;
import com.ssblur.scriptor.blockentity.EngravingBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class EngravingBlockEntityRenderer implements BlockEntityRenderer<EngravingBlockEntity> {
  static ResourceLocation magicCircleS = new ResourceLocation(ScriptorMod.MOD_ID, "textures/entity/engraving_edge.png");
  static ResourceLocation magicCircleE = new ResourceLocation(ScriptorMod.MOD_ID, "textures/entity/engraving_edge_2.png");
  static ResourceLocation magicCircleN = new ResourceLocation(ScriptorMod.MOD_ID, "textures/entity/engraving_edge_3.png");
  static ResourceLocation magicCircleW = new ResourceLocation(ScriptorMod.MOD_ID, "textures/entity/engraving_edge_4.png");
  static RenderType circleLayerN =
    RenderType
      .create(
        ScriptorMod.MOD_ID + ":engraving_edge_north",
        DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
        VertexFormat.Mode.QUADS,
        64,
        false,
        false,
        RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(magicCircleN, false, false))
          .setCullState(new RenderStateShard.CullStateShard(false))
          .setLightmapState(new RenderStateShard.LightmapStateShard(true))
          .setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER)
          .createCompositeState(true)
      );
  static RenderType circleLayerS =
    RenderType
      .create(
        ScriptorMod.MOD_ID + ":engraving_edge_south",
        DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
        VertexFormat.Mode.QUADS,
        64,
        false,
        false,
        RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(magicCircleS, false, false))
          .setCullState(new RenderStateShard.CullStateShard(false))
          .setLightmapState(new RenderStateShard.LightmapStateShard(true))
          .setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER)
          .createCompositeState(true)
      );
  static RenderType circleLayerE =
    RenderType
      .create(
        ScriptorMod.MOD_ID + ":engraving_edge_east",
        DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
        VertexFormat.Mode.QUADS,
        64,
        false,
        false,
        RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(magicCircleE, false, false))
          .setCullState(new RenderStateShard.CullStateShard(false))
          .setLightmapState(new RenderStateShard.LightmapStateShard(true))
          .setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER)
          .createCompositeState(true)
      );
  static RenderType circleLayerW =
    RenderType
      .create(
        ScriptorMod.MOD_ID + ":engraving_edge_west",
        DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
        VertexFormat.Mode.QUADS,
        64,
        false,
        false,
        RenderType.CompositeState.builder().setTextureState(new RenderStateShard.TextureStateShard(magicCircleW, false, false))
          .setCullState(new RenderStateShard.CullStateShard(false))
          .setLightmapState(new RenderStateShard.LightmapStateShard(true))
          .setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER)
          .createCompositeState(true)
      );

  public EngravingBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

  @Override
  public void render(EngravingBlockEntity blockEntity, float tickDelta, PoseStack matrix, MultiBufferSource buffers, int light, int j) {
    var level = blockEntity.getLevel();
    var pos = blockEntity.getBlockPos();
    if(level == null) return;
    matrix.pushPose();

    var rotationY = 0;
    switch (blockEntity.getFacing()) {
      case SOUTH -> rotationY = 180;
      case WEST -> rotationY = 90;
      case EAST -> rotationY = 270;
    }

    matrix.translate(0f, 0.01f, 0f);

    var pose = matrix.last().pose();
    if(level.getBlockState(pos.north()).is(ScriptorBlocks.ENGRAVING.get())) {
      var buffer = buffers.getBuffer(circleLayerN);
      buffer.vertex(pose, 0, 0, 1).color(255, 255, 255, 255).uv(0, 1).uv2(0xF000F0).endVertex();
      buffer.vertex(pose, 1, 0, 1).color(255, 255, 255, 255).uv(1, 1).uv2(0xF000F0).endVertex();
      buffer.vertex(pose, 1, 0, 0).color(255, 255, 255, 255).uv(1, 0).uv2(0xF000F0).endVertex();
      buffer.vertex(pose, 0, 0, 0).color(255, 255, 255, 255).uv(0, 0).uv2(0xF000F0).endVertex();
    }

    if(level.getBlockState(pos.south()).is(ScriptorBlocks.ENGRAVING.get())) {
      var buffer = buffers.getBuffer(circleLayerS);
      buffer.vertex(pose, 0, 0, 1).color(255, 255, 255, 255).uv(0, 1).uv2(0xF000F0).endVertex();
      buffer.vertex(pose, 1, 0, 1).color(255, 255, 255, 255).uv(1, 1).uv2(0xF000F0).endVertex();
      buffer.vertex(pose, 1, 0, 0).color(255, 255, 255, 255).uv(1, 0).uv2(0xF000F0).endVertex();
      buffer.vertex(pose, 0, 0, 0).color(255, 255, 255, 255).uv(0, 0).uv2(0xF000F0).endVertex();
    }

    if(level.getBlockState(pos.east()).is(ScriptorBlocks.ENGRAVING.get())) {
      var buffer = buffers.getBuffer(circleLayerE);
      buffer.vertex(pose, 0, 0, 1).color(255, 255, 255, 255).uv(0, 1).uv2(0xF000F0).endVertex();
      buffer.vertex(pose, 1, 0, 1).color(255, 255, 255, 255).uv(1, 1).uv2(0xF000F0).endVertex();
      buffer.vertex(pose, 1, 0, 0).color(255, 255, 255, 255).uv(1, 0).uv2(0xF000F0).endVertex();
      buffer.vertex(pose, 0, 0, 0).color(255, 255, 255, 255).uv(0, 0).uv2(0xF000F0).endVertex();
    }

    if(level.getBlockState(pos.west()).is(ScriptorBlocks.ENGRAVING.get())) {
      var buffer = buffers.getBuffer(circleLayerW);
      buffer.vertex(pose, 0, 0, 1).color(255, 255, 255, 255).uv(0, 1).uv2(0xF000F0).endVertex();
      buffer.vertex(pose, 1, 0, 1).color(255, 255, 255, 255).uv(1, 1).uv2(0xF000F0).endVertex();
      buffer.vertex(pose, 1, 0, 0).color(255, 255, 255, 255).uv(1, 0).uv2(0xF000F0).endVertex();
      buffer.vertex(pose, 0, 0, 0).color(255, 255, 255, 255).uv(0, 0).uv2(0xF000F0).endVertex();
    }

    matrix.translate(0.5f, 0f, 0.5f);

    matrix.mulPose(Axis.YP.rotationDegrees(rotationY));
    matrix.mulPose(Axis.XP.rotationDegrees(90));
    matrix.scale(0.015f, 0.015f, 0.015f);

    var font = Minecraft.getInstance().font;
    Component text = Component.literal(blockEntity.getWord());

    var width = font.width(text);
    matrix.translate(-width * 0.5f, -6f, 0f);

    font.drawInBatch(
      text,
      0,
      0,
      0x404040,
      false,
      matrix.last().pose(),
      buffers,
      Font.DisplayMode.NORMAL,
      0x0,
      light
    );

    matrix.popPose();
  }
}
