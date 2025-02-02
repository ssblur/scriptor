package com.ssblur.scriptor.events

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.*
import com.ssblur.scriptor.item.ScriptorItems.COORDINATE_CASTING_CRYSTAL
import com.ssblur.scriptor.item.casters.CoordinateCasterCrystal.Companion.getCoordinates
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.core.Direction
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.ItemStack

object CoordinateCasterWorldRenderer {
  const val BOX_COLOR: Int = -0xaf9c

  fun render(matrix: PoseStack?) {
    val player = Minecraft.getInstance().player

    if (player != null && matrix != null) {
      if (player.getItemInHand(InteractionHand.MAIN_HAND).item === COORDINATE_CASTING_CRYSTAL.get()) render(
        matrix, player.getItemInHand(
          InteractionHand.MAIN_HAND
        )
      )
      if (player.getItemInHand(InteractionHand.OFF_HAND).item === COORDINATE_CASTING_CRYSTAL.get()) render(
        matrix, player.getItemInHand(
          InteractionHand.OFF_HAND
        )
      )
    }
  }

  fun render(matrix: PoseStack, item: ItemStack?) {
    val tesselator = Tesselator.getInstance()
    var builder: BufferBuilder
    var mesh: MeshData?
    val camera = Minecraft.getInstance().gameRenderer.mainCamera
    val last = matrix.last()
    val pose = last.pose()

    matrix.pushPose()
    RenderSystem.disableDepthTest()
    RenderSystem.enableBlend()
    RenderSystem.setShader { GameRenderer.getPositionColorShader() }
    RenderSystem.disableCull()
    for (pair in getCoordinates(item!!)) {
      val pos = pair.left
      val direction = pair.right

      var x = (pos.x - camera.position.x).toFloat()
      var y = (pos.y - camera.position.y + 0.01f).toFloat()
      var z = (pos.z - camera.position.z).toFloat()
      var xo = x + 1
      var yo = y + 1
      val zo = z + 1

      when (direction) {
        Direction.DOWN -> y += 1f
        Direction.UP -> yo -= 1f
        Direction.NORTH -> z += 1f
        Direction.WEST -> x += 1f
        Direction.EAST -> xo -= 1f
        Direction.SOUTH -> {}
      }
      if (direction.axis === Direction.Axis.Z) {
        builder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR)

        builder.addVertex(
          pose,
          x,
          y,
          z
        ).setColor(BOX_COLOR)
        builder.addVertex(
          pose,
          xo,
          y,
          z
        ).setColor(BOX_COLOR)
        builder.addVertex(
          pose,
          xo,
          yo,
          z
        ).setColor(BOX_COLOR)
        builder.addVertex(
          pose,
          x,
          yo,
          z
        ).setColor(BOX_COLOR)

        mesh = builder.build()
      } else {
        builder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR)

        builder.addVertex(
          pose,
          x,
          y,
          z
        ).setColor(BOX_COLOR)
        builder.addVertex(
          pose,
          x,
          y,
          zo
        ).setColor(BOX_COLOR)
        builder.addVertex(
          pose,
          xo,
          yo,
          zo
        ).setColor(BOX_COLOR)
        builder.addVertex(
          pose,
          xo,
          yo,
          z
        ).setColor(BOX_COLOR)

        mesh = builder.build()
      }

      if (mesh != null) {
        BufferUploader.drawWithShader(mesh)
      }
    }

    matrix.popPose()
  }
}