package com.ssblur.scriptor.item.casters

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import com.ssblur.scriptor.block.ScriptorBlocks
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.helpers.ComponentHelper
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.item.interfaces.ItemWithCustomRenderer
import com.ssblur.scriptor.network.server.TraceNetwork
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.DirectionalBlock
import org.apache.commons.lang3.tuple.Pair
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class CoordinateCasterCrystal(properties: Properties): CasterCrystal(properties), ItemWithCustomRenderer {
  class BlockPosDirection(var blockPos: BlockPos, var direction: Direction): Pair<BlockPos, Direction>() {
    override fun getLeft(): BlockPos {
      return blockPos
    }

    override fun getRight(): Direction {
      return direction
    }

    override fun setValue(newValue: Direction): Direction {
      return newValue.also { direction = it }
    }

    fun setValue(newValue: BlockPos): BlockPos {
      return newValue.also { blockPos = it }
    }
  }

  override fun getTargetables(itemStack: ItemStack?, level: Level?): List<Targetable?> {
    val list = ArrayList<Targetable?>()
    val coordinates = itemStack!!.get(ScriptorDataComponents.COORDINATES)
    if (coordinates != null) for (pos in coordinates) {
      val targetable = Targetable(
        level!!,
        BlockPos(pos[0], pos[1], pos[2])
      )
      targetable.setFacing(Direction.entries[pos[3]])
      list.add(targetable)
    }
    return list
  }

  override fun appendHoverText(
    itemStack: ItemStack,
    level: TooltipContext,
    list: MutableList<Component>,
    tooltipFlag: TooltipFlag
  ) {
    super.appendHoverText(itemStack, level, list, tooltipFlag)
    val coordinates = getCoordinates(itemStack)
    for (pair in coordinates) {
      val coordinate = pair.getLeft()
      ComponentHelper.updateTooltipWith(
        ChatFormatting.GRAY,
        list,
        "lore.scriptor.coordinate_crystal_3",
        coordinate.x,
        coordinate.y,
        coordinate.z
      )
    }

    if (coordinates.isEmpty()) list.add(
      Component.translatable("lore.scriptor.coordinate_crystal_1").withStyle(
        ChatFormatting.GRAY
      )
    )
    else {
      if (coordinates.size < 4) list.add(
        Component.translatable("lore.scriptor.coordinate_crystal_2").withStyle(
          ChatFormatting.GRAY
        )
      )
      list.add(Component.translatable("lore.scriptor.crystal_reset").withStyle(ChatFormatting.GRAY))
    }

    list.add(Component.translatable("lore.scriptor.crystal_focus").withStyle(ChatFormatting.GRAY))

  }

  override fun use(
    level: Level,
    player: Player,
    interactionHand: InteractionHand
  ): InteractionResultHolder<ItemStack> {
    val result = super.use(level, player, interactionHand)

    if (!level.isClientSide) {
      val itemStack = player.getItemInHand(interactionHand)
      TraceNetwork.requestTraceData(player, false) { target: Targetable ->
        addCoordinate(
          itemStack,
          target.targetBlockPos,
          target.facing
        )
      }
    }

    return result
  }

  companion object {
    fun addCoordinate(itemStack: ItemStack, pos: BlockPos, direction: Direction) {
      var list = itemStack.get(ScriptorDataComponents.COORDINATES)
      if (list == null) list = ArrayList()
      if (list.size < 4) {
        list = ArrayList(list)
        list.add(java.util.List.of(pos.x, pos.y, pos.z, direction.ordinal))
      }
      itemStack.set(ScriptorDataComponents.COORDINATES, list)
    }

    @JvmStatic
    fun getCoordinates(itemStack: ItemStack): List<BlockPosDirection> {
      val list = ArrayList<BlockPosDirection>()
      val coordinates = itemStack.get(ScriptorDataComponents.COORDINATES)
      if (coordinates != null) for (pos in coordinates) list.add(
        BlockPosDirection(
          BlockPos(
            pos[0],
            pos[1],
            pos[2]
          ), Direction.entries[pos[3]]
        )
      )
      return list
    }
  }

  override fun render(
    player: AbstractClientPlayer,
    i: Float,
    pitch: Float,
    hand: InteractionHand,
    swingProgress: Float,
    itemStack: ItemStack,
    readyProgress: Float,
    matrix: PoseStack?,
    buffer: MultiBufferSource,
    lightLevel: Int
  ): Boolean {
    if(matrix == null) return false
    val minecraft = Minecraft.getInstance()
    val dispatcher = minecraft.blockRenderer
    val gameRenderer = minecraft.gameRenderer
    val camera = gameRenderer.mainCamera.position
    matrix.pushPose()
    matrix.setIdentity()
    val g = player.walkDist - player.walkDistO
    val h = -(player.walkDist + g * i).toDouble()
    val j = Mth.lerp(i, player.oBob, player.bob)
    matrix.translate(
      (sin(h * Math.PI) * j * 0.5),
      -abs((cos(h * Math.PI) * j)),
      0.0
    )
    matrix.translate(-camera.x, -camera.y, -camera.z)

    RenderSystem.disableDepthTest()
    RenderSystem.enableBlend()
    RenderSystem.setShader { GameRenderer.getPositionTexColorShader() }
    RenderSystem.disableCull()

    for ((pos, dir) in getCoordinates(itemStack)) {
      matrix.pushPose()
      val blockState = ScriptorBlocks.HIGHLIGHT_MODEL.get().defaultBlockState().setValue(DirectionalBlock.FACING, dir)
      matrix.translate(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
      dispatcher.renderSingleBlock(blockState, matrix, buffer, 0xffffff, 0)
      matrix.popPose()
    }

    matrix.popPose()
    return false
  }
}
