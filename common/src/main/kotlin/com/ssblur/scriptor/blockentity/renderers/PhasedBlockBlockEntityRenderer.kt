package com.ssblur.scriptor.blockentity.renderers

import com.mojang.blaze3d.vertex.PoseStack
import com.ssblur.scriptor.blockentity.PhasedBlockBlockEntity
import com.ssblur.scriptor.mixin.BlockRenderDispatcherAccessor
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.Sheets
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.Direction
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource

class PhasedBlockBlockEntityRenderer(var context: BlockEntityRendererProvider.Context):
  BlockEntityRenderer<PhasedBlockBlockEntity> {
  override fun render(
    blockEntity: PhasedBlockBlockEntity,
    f: Float,
    matrix: PoseStack,
    multiBufferSource: MultiBufferSource,
    i: Int,
    j: Int
  ) {
    @Suppress("SENSELESS_COMPARISON")
    if (matrix == null || blockEntity == null) return
    val blockState = blockEntity.phasedBlockState
    val level = blockEntity.level

    if (blockState == null || level == null) return

    val dispatcher = context.blockRenderDispatcher
    val buffer = multiBufferSource.getBuffer(Sheets.translucentCullBlockSheet())
    val model = dispatcher.getBlockModel(blockState)
    val randomSource = RandomSource.create()
    val pose = matrix.last()


    val color = (dispatcher as BlockRenderDispatcherAccessor).blockColors.getColor(blockState, null, null, 0)
    val r = (color shr 16 and 0xFF) / 255.0f
    val g = (color shr 8 and 0xFF) / 255.0f
    val b = (color and 0xFF) / 255.0f
    val a = 1.0f - (0.8f * blockEntity.anim)
    var ra: Float
    var ga: Float
    var ba: Float

    for (direction in Direction.entries) {
      randomSource.setSeed(42L)
      for (bakedQuad in model.getQuads(blockState, direction, randomSource)) {
        if (bakedQuad.isTinted) {
          ra = Mth.clamp(r, 0.0f, 1.0f)
          ga = Mth.clamp(g, 0.0f, 1.0f)
          ba = Mth.clamp(b, 0.0f, 1.0f)
        } else {
          ra = 1.0f
          ga = 1.0f
          ba = 1.0f
        }
        buffer.putBulkData(pose, bakedQuad, ra, ga, ba, a, i, j)
      }
    }

    randomSource.setSeed(42L)
    for (bakedQuad in model.getQuads(blockState, null, randomSource)) {
      if (bakedQuad.isTinted) {
        ra = Mth.clamp(r, 0.0f, 1.0f)
        ga = Mth.clamp(g, 0.0f, 1.0f)
        ba = Mth.clamp(b, 0.0f, 1.0f)
      } else {
        ra = 1.0f
        ga = 1.0f
        ba = 1.0f
      }
      buffer.putBulkData(pose, bakedQuad, ra, ga, ba, a, i, j)
    }
  }
}
