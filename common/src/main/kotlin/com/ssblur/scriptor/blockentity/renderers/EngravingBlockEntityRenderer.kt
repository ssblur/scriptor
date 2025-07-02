package com.ssblur.scriptor.blockentity.renderers

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexFormat
import com.mojang.math.Axis
import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.block.EngravingBlock
import com.ssblur.scriptor.block.ScriptorBlocks
import com.ssblur.scriptor.blockentity.EngravingBlockEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

class EngravingBlockEntityRenderer(@Suppress("unused_parameter") context: BlockEntityRendererProvider.Context?):
  BlockEntityRenderer<EngravingBlockEntity> {
  override fun render(
    blockEntity: EngravingBlockEntity,
    tickDelta: Float,
    matrix: PoseStack,
    buffers: MultiBufferSource,
    light: Int,
    j: Int
  ) {
    @Suppress("SENSELESS_COMPARISON")
    if (matrix == null) return
    val level = blockEntity.level
    val pos = blockEntity.blockPos
    if (level == null) return
    matrix.pushPose()

    var rotationY = 0
    when (blockEntity.facing) {
      Direction.SOUTH -> rotationY = 180
      Direction.WEST -> rotationY = 90
      Direction.EAST -> rotationY = 270
      else -> {}
    }
    matrix.translate(0f, 0.01f, 0f)

    val pose = matrix.last().pose()
    if (level.getBlockState(pos.north()).`is`(ScriptorBlocks.ENGRAVING.get())) {
      val buffer = buffers.getBuffer(circleLayerN)
      buffer.addVertex(pose, 0f, 0f, 1f).setColor(255, 255, 255, 255).setUv(0f, 1f).setLight(0xF000F0)
      buffer.addVertex(pose, 1f, 0f, 1f).setColor(255, 255, 255, 255).setUv(1f, 1f).setLight(0xF000F0)
      buffer.addVertex(pose, 1f, 0f, 0f).setColor(255, 255, 255, 255).setUv(1f, 0f).setLight(0xF000F0)
      buffer.addVertex(pose, 0f, 0f, 0f).setColor(255, 255, 255, 255).setUv(0f, 0f).setLight(0xF000F0)
    }

    if (level.getBlockState(pos.south()).`is`(ScriptorBlocks.ENGRAVING.get())) {
      val buffer = buffers.getBuffer(circleLayerS)
      buffer.addVertex(pose, 0f, 0f, 1f).setColor(255, 255, 255, 255).setUv(0f, 1f).setLight(0xF000F0)
      buffer.addVertex(pose, 1f, 0f, 1f).setColor(255, 255, 255, 255).setUv(1f, 1f).setLight(0xF000F0)
      buffer.addVertex(pose, 1f, 0f, 0f).setColor(255, 255, 255, 255).setUv(1f, 0f).setLight(0xF000F0)
      buffer.addVertex(pose, 0f, 0f, 0f).setColor(255, 255, 255, 255).setUv(0f, 0f).setLight(0xF000F0)
    }

    if (level.getBlockState(pos.east()).`is`(ScriptorBlocks.ENGRAVING.get())) {
      val buffer = buffers.getBuffer(circleLayerE)
      buffer.addVertex(pose, 0f, 0f, 1f).setColor(255, 255, 255, 255).setUv(0f, 1f).setLight(0xF000F0)
      buffer.addVertex(pose, 1f, 0f, 1f).setColor(255, 255, 255, 255).setUv(1f, 1f).setLight(0xF000F0)
      buffer.addVertex(pose, 1f, 0f, 0f).setColor(255, 255, 255, 255).setUv(1f, 0f).setLight(0xF000F0)
      buffer.addVertex(pose, 0f, 0f, 0f).setColor(255, 255, 255, 255).setUv(0f, 0f).setLight(0xF000F0)
    }

    if (level.getBlockState(pos.west()).`is`(ScriptorBlocks.ENGRAVING.get())) {
      val buffer = buffers.getBuffer(circleLayerW)
      buffer.addVertex(pose, 0f, 0f, 1f).setColor(255, 255, 255, 255).setUv(0f, 1f).setLight(0xF000F0)
      buffer.addVertex(pose, 1f, 0f, 1f).setColor(255, 255, 255, 255).setUv(1f, 1f).setLight(0xF000F0)
      buffer.addVertex(pose, 1f, 0f, 0f).setColor(255, 255, 255, 255).setUv(1f, 0f).setLight(0xF000F0)
      buffer.addVertex(pose, 0f, 0f, 0f).setColor(255, 255, 255, 255).setUv(0f, 0f).setLight(0xF000F0)
    }

    matrix.translate(0.5f, 0f, 0.5f)

    matrix.mulPose(Axis.YP.rotationDegrees(rotationY.toFloat()))
    matrix.mulPose(Axis.XP.rotationDegrees(90f))
    matrix.scale(0.015f, 0.015f, 0.015f)

    val font = Minecraft.getInstance().font
    val text: Component = Component.literal(blockEntity.word)

    val width = font.width(text)
    matrix.translate(-width * 0.5f, -6f, 0f)

    var color = 0x707070
    if (blockEntity.blockState.getValue(EngravingBlock.HIGHLIGHT)) color = 0xFFB600
    if (blockEntity.blockState.getValue(EngravingBlock.SPENT)) color = 0x303030
    font.drawInBatch(
      text,
      0f,
      0f,
      color,
      false,
      matrix.last().pose(),
      buffers,
      Font.DisplayMode.NORMAL,
      0x0,
      light
    )

    matrix.popPose()
  }

  companion object {
    var magicCircleS: ResourceLocation = ScriptorMod.location("textures/entity/engraving_edge.png")
    var magicCircleE: ResourceLocation = ScriptorMod.location("textures/entity/engraving_edge_2.png")
    var magicCircleN: ResourceLocation = ScriptorMod.location("textures/entity/engraving_edge_3.png")
    var magicCircleW: ResourceLocation = ScriptorMod.location("textures/entity/engraving_edge_4.png")
    var circleLayerN: RenderType = RenderType
      .create(
        ScriptorMod.MOD_ID + ":engraving_edge_north",
        DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
        VertexFormat.Mode.QUADS,
        64,
        false,
        false,
        RenderType.CompositeState.builder()
          .setTextureState(RenderStateShard.TextureStateShard(magicCircleN, false, false))
          .setCullState(RenderStateShard.CullStateShard(false))
          .setLightmapState(RenderStateShard.LightmapStateShard(true))
          .setShaderState(RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
          .createCompositeState(true)
      )
    var circleLayerS: RenderType = RenderType
      .create(
        ScriptorMod.MOD_ID + ":engraving_edge_south",
        DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
        VertexFormat.Mode.QUADS,
        64,
        false,
        false,
        RenderType.CompositeState.builder()
          .setTextureState(RenderStateShard.TextureStateShard(magicCircleS, false, false))
          .setCullState(RenderStateShard.CullStateShard(false))
          .setLightmapState(RenderStateShard.LightmapStateShard(true))
          .setShaderState(RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
          .createCompositeState(true)
      )
    var circleLayerE: RenderType = RenderType
      .create(
        ScriptorMod.MOD_ID + ":engraving_edge_east",
        DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
        VertexFormat.Mode.QUADS,
        64,
        false,
        false,
        RenderType.CompositeState.builder()
          .setTextureState(RenderStateShard.TextureStateShard(magicCircleE, false, false))
          .setCullState(RenderStateShard.CullStateShard(false))
          .setLightmapState(RenderStateShard.LightmapStateShard(true))
          .setShaderState(RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
          .createCompositeState(true)
      )
    var circleLayerW: RenderType = RenderType
      .create(
        ScriptorMod.MOD_ID + ":engraving_edge_west",
        DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
        VertexFormat.Mode.QUADS,
        64,
        false,
        false,
        RenderType.CompositeState.builder()
          .setTextureState(RenderStateShard.TextureStateShard(magicCircleW, false, false))
          .setCullState(RenderStateShard.CullStateShard(false))
          .setLightmapState(RenderStateShard.LightmapStateShard(true))
          .setShaderState(RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
          .createCompositeState(true)
      )
  }
}
