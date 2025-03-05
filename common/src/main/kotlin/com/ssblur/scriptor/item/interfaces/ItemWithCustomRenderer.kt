package com.ssblur.scriptor.item.interfaces

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.ItemStack

interface ItemWithCustomRenderer {
  /**
   * Render this custom item.
   * @param player The local player holding the item
   * @param i Unknown, will look at source for info
   * @param pitch The pitch at which this item should be rendered
   * @param hand The InteractionHand holding this item (MainHand / OffHand)
   * @param swingProgress How far this item is in the swing animation.
   * @param itemStack The ItemStack being rendered
   * @param readyProgress How close this item is to being usable after selected in the hotbar
   * @param matrix The PoseStack used for rendering
   * @param buffer The MultiBufferSource used for rendering
   * @param lightLevel The light level at the item
   * @return true to cancel further rendering, false to continue rendering as normal
   */
  fun render(
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
  ): Boolean = false

  fun worldRender(
    player: AbstractClientPlayer,
    hand: InteractionHand,
    itemStack: ItemStack,
    matrix: PoseStack?,
    buffer: MultiBufferSource
  ) {}
}
