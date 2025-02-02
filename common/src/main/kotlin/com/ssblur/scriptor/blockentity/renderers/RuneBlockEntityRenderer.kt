package com.ssblur.scriptor.blockentity.renderers

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexFormat
import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.blockentity.RuneBlockEntity
import com.ssblur.scriptor.color.CustomColors.getColor
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.resources.ResourceLocation
import kotlin.math.cos
import kotlin.math.sin

class RuneBlockEntityRenderer(@Suppress("unused_parameter") context: BlockEntityRendererProvider.Context?):
  BlockEntityRenderer<RuneBlockEntity> {
  override fun render(
    rune: RuneBlockEntity,
    tickDelta: Float,
    matrix: PoseStack,
    buffers: MultiBufferSource,
    light: Int,
    j: Int
  ) {
    if (rune.level == null) return
    @Suppress("SENSELESS_COMPARISON")
    if (matrix == null) return
    matrix.pushPose()

    val c = getColor(rune.runeColor, rune.level!!.gameTime + tickDelta)
    val mc = Minecraft.getInstance()
    checkNotNull(mc.level)
    val r = c and 0xff0000 shr 16
    val g = c and 0x00ff00 shr 8
    val b = c and 0x0000ff
    val yo = 0.0625f
    matrix.translate(0f, yo, 0f)
    val pose = matrix.last().pose()
    var buffer = buffers.getBuffer(magicLayer)

    val cycle = 240
    var rot = (rune.level!!.gameTime % cycle).toDouble()
    rot = ((rot + tickDelta) / cycle) * 2 * Math.PI
    var R = 0.5
    var x = (R * cos(rot)).toFloat()
    var y = (R * sin(rot)).toFloat()
    buffer.addVertex(pose, x + 0.5f, 0f, y + 0.5f).setColor(r, g, b, 255).setUv(0f, 1f).setLight(0xF000F0)
    rot += (Math.PI / 2)
    x = (R * cos(rot)).toFloat()
    y = (R * sin(rot)).toFloat()
    buffer.addVertex(pose, x + 0.5f, 0f, y + 0.5f).setColor(r, g, b, 255).setUv(1f, 1f).setLight(0xF000F0)
    rot += (Math.PI / 2)
    x = (R * cos(rot)).toFloat()
    y = (R * sin(rot)).toFloat()
    buffer.addVertex(pose, x + 0.5f, 0f, y + 0.5f).setColor(r, g, b, 255).setUv(1f, 0f).setLight(0xF000F0)
    rot += (Math.PI / 2)
    x = (R * cos(rot)).toFloat()
    y = (R * sin(rot)).toFloat()
    buffer.addVertex(pose, x + 0.5f, 0f, y + 0.5f).setColor(r, g, b, 255).setUv(0f, 0f).setLight(0xF000F0)

    buffer = buffers.getBuffer(emptyLayer)
    R = 0.2
    rot *= -1.0
    x = (R * cos(rot)).toFloat()
    y = (R * sin(rot)).toFloat()
    buffer.addVertex(pose, x + 0.5f, 0f, y + 0.5f).setColor(r, g, b, 255).setUv(0f, 1f).setLight(0xF000F0)
    rot += (Math.PI / 2)
    x = (R * cos(rot)).toFloat()
    y = (R * sin(rot)).toFloat()
    buffer.addVertex(pose, x + 0.5f, 0f, y + 0.5f).setColor(r, g, b, 255).setUv(1f, 1f).setLight(0xF000F0)
    rot += (Math.PI / 2)
    x = (R * cos(rot)).toFloat()
    y = (R * sin(rot)).toFloat()
    buffer.addVertex(pose, x + 0.5f, 0f, y + 0.5f).setColor(r, g, b, 255).setUv(1f, 0f).setLight(0xF000F0)
    rot += (Math.PI / 2)
    x = (R * cos(rot)).toFloat()
    y = (R * sin(rot)).toFloat()
    buffer.addVertex(pose, x + 0.5f, 0f, y + 0.5f).setColor(r, g, b, 255).setUv(0f, 0f).setLight(0xF000F0)

    matrix.popPose()
  }

  companion object {
    var magicCircle: ResourceLocation = ScriptorMod.location("textures/entity/magic_circle.png")
    var emptyCircle: ResourceLocation = ScriptorMod.location("textures/entity/empty_circle.png")
    var magicLayer: RenderType = RenderType
      .create(
        ScriptorMod.MOD_ID + ":rune",
        DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
        VertexFormat.Mode.QUADS,
        64,
        false,
        false,
        RenderType.CompositeState.builder()
          .setTextureState(RenderStateShard.TextureStateShard(magicCircle, false, false))
          .setCullState(RenderStateShard.CullStateShard(false))
          .setLightmapState(RenderStateShard.LightmapStateShard(true))
          .setShaderState(RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
          .createCompositeState(true)
      )
    var emptyLayer: RenderType = RenderType
      .create(
        ScriptorMod.MOD_ID + ":circle",
        DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
        VertexFormat.Mode.QUADS,
        64,
        false,
        false,
        RenderType.CompositeState.builder()
          .setTextureState(RenderStateShard.TextureStateShard(emptyCircle, false, false))
          .setCullState(RenderStateShard.CullStateShard(false))
          .setLightmapState(RenderStateShard.LightmapStateShard(true))
          .setShaderState(RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
          .createCompositeState(true)
      )
  }
}
