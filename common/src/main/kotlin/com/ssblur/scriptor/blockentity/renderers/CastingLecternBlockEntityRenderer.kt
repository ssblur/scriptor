package com.ssblur.scriptor.blockentity.renderers

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import com.ssblur.scriptor.block.CastingLecternBlock
import com.ssblur.scriptor.blockentity.CastingLecternBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemDisplayContext
import kotlin.math.sin

class CastingLecternBlockEntityRenderer(context: BlockEntityRendererProvider.Context):
  BlockEntityRenderer<CastingLecternBlockEntity> {
  var itemRenderer: ItemRenderer = context.itemRenderer
  override fun render(
    lectern: CastingLecternBlockEntity,
    tickDelta: Float,
    matrix: PoseStack,
    buffers: MultiBufferSource,
    light: Int,
    overlay: Int
  ) {
    val level = lectern.level
    if (lectern.isRemoved || level == null) return
    var time = level.gameTime + tickDelta
    time %= 101f
    time /= 100f

    matrix.pushPose()

    val combined = lectern.blockPos.x + lectern.blockPos.y + lectern.blockPos.z
    var translateY = sin((combined + level.gameTime + tickDelta.toDouble()) / 8)
      .toFloat()
    translateY /= 64f

    var rotationY = 0
    var translateX = 0f
    var translateZ = 0f

    val state = level.getBlockState(lectern.blockPos)
    when (state.getValue(CastingLecternBlock.FACING)) {
      Direction.NORTH -> {
        translateZ -= 0.2f
        rotationY += 180
      }

      Direction.SOUTH -> translateZ += 0.2f
      Direction.WEST -> {
        rotationY += 270
        translateX -= 0.2f
      }

      else -> {
        rotationY += 90
        translateX += 0.2f
      }
    }
    matrix.pushPose()

    matrix.translate(0.5f, 1.3f, 0.5f)
    matrix.scale(0.5f, 0.5f, 0.5f)

    matrix.mulPose(Axis.YP.rotationDegrees(time * 360))
    itemRenderer.renderStatic(
      lectern.focus,
      ItemDisplayContext.GROUND,
      light,
      overlay,
      matrix,
      buffers,
      level,
      lectern.blockPos.asLong().toInt()
    )

    matrix.popPose()

    matrix.translate(0.5f + translateX, 1f + translateY, 0.5f + translateZ)

    matrix.mulPose(Axis.YP.rotationDegrees(rotationY.toFloat()))
    matrix.mulPose(Axis.XP.rotationDegrees(112f))

    itemRenderer.renderStatic(
      lectern.spellbook,
      ItemDisplayContext.FIXED,
      light,
      overlay,
      matrix,
      buffers,
      level,
      lectern.blockPos.asLong().toInt()
    )

    matrix.popPose()
  }
}
