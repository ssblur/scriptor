package com.ssblur.scriptor.item.books

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import com.ssblur.scriptor.ScriptorMod.LOGGER
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.helpers.ComponentHelper
import com.ssblur.scriptor.helpers.LimitedBookSerializer
import com.ssblur.scriptor.helpers.SpellbookHelper
import com.ssblur.scriptor.item.ScriptorTabs
import com.ssblur.scriptor.item.interfaces.ItemWithCustomRenderer
import com.ssblur.scriptor.network.server.ScriptorNetworkC2S
import com.ssblur.scriptor.network.server.ScriptorNetworkC2S.UseBook
import com.ssblur.unfocused.tab.CreativeTabs.tab
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.resources.language.I18n
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FormattedText
import net.minecraft.util.FormattedCharSequence
import net.minecraft.util.Mth
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ClickAction
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.WrittenBookItem
import net.minecraft.world.level.Level

@Suppress("unstable")
open class Spellbook(properties: Properties):
  WrittenBookItem(properties),
  ItemWithCustomRenderer {
  init {
    tab(ScriptorTabs.SCRIPTOR_SPELLBOOKS_TAB)
    SpellbookHelper.SPELLBOOKS += this
  }

  override fun use(
    level: Level,
    player: Player,
    interactionHand: InteractionHand
  ): InteractionResultHolder<ItemStack> {
    if (level.isClientSide) return InteractionResultHolder.success(player.getItemInHand(interactionHand))

    val item = player.getItemInHand(interactionHand)
    SpellbookHelper.castFromItem(item, player)

    return InteractionResultHolder.fail(player.getItemInHand(interactionHand))
  }

  override fun getName(itemStack: ItemStack): Component {
    val title = itemStack.get(ScriptorDataComponents.TOME_NAME)
    if (title != null) {
      return Component.translatable(title)
    }
    return super.getName(itemStack)
  }

  override fun overrideStackedOnOther(
    itemStack: ItemStack,
    slot: Slot,
    clickAction: ClickAction,
    player: Player
  ): Boolean {
    if (clickAction == ClickAction.SECONDARY && !slot.item.isEmpty && slot.item.item !is BookOfBooks) {
      if (player.cooldowns.isOnCooldown(this)) return true
      val level = player.level()
      if (!level.isClientSide) return true
      if (player.isCreative) return false // TODO
      else ScriptorNetworkC2S.useBook(UseBook(slot.index))
      return true
    }
    return false
  }

  override fun appendHoverText(
    itemStack: ItemStack,
    level: TooltipContext,
    list: MutableList<Component>,
    tooltipFlag: TooltipFlag
  ) {
    super.appendHoverText(itemStack, level, list, tooltipFlag)

    if (itemStack.get(DataComponents.WRITTEN_BOOK_CONTENT) == null) list.add(
      Component.translatable("lore.scriptor.no_spell").withStyle(
        ChatFormatting.GRAY
      )
    )

    val identified = itemStack.get(ScriptorDataComponents.IDENTIFIED)
    if (identified != null) {
      if (Screen.hasShiftDown()) for (key in identified) {
        val parts = key.split(":".toRegex(), limit = 2).toTypedArray()
        if (parts.size == 2) ComponentHelper.updateTooltipWith(list, parts[0] + ".scriptor." + parts[1])
        else LOGGER.error("Invalid Identify entry: $key")
      }
      else ComponentHelper.updateTooltipWith(list, "extra.scriptor.tome_identified")
      ComponentHelper.addCommunityDisclaimer(list, itemStack)
    }
  }

  override fun render(
    player: AbstractClientPlayer?,
    i: Float,
    pitch: Float,
    hand: InteractionHand?,
    swingProgress: Float,
    itemStack: ItemStack?,
    readyProgress: Float,
    matrix: PoseStack?,
    buffer: MultiBufferSource,
    lightLevel: Int
  ): Boolean {
    if (matrix == null || itemStack == null || player == null) return false
    matrix.pushPose()
    val inRightHand = (hand == InteractionHand.MAIN_HAND) xor (player.mainArm == HumanoidArm.LEFT)
    val v = if (inRightHand) 1 else -1
    matrix.translate(v.toFloat() * 0.56f, -0.52f + readyProgress * -0.6f, -0.72f)
    val g = Mth.sin(swingProgress * swingProgress * Math.PI.toFloat())
    matrix.mulPose(Axis.YP.rotationDegrees(i * (45.0f + g * -20.0f)))
    val h = Mth.sin(Mth.sqrt(swingProgress) * Math.PI.toFloat())
    matrix.mulPose(Axis.ZP.rotationDegrees(i * h * -20.0f))
    matrix.mulPose(Axis.XP.rotationDegrees(h * -80.0f))
    matrix.mulPose(Axis.YP.rotationDegrees(i * -45.0f))

    if (inRightHand) {
      run {
        matrix.pushPose() // Right hand, left page

        matrix.translate(-0.30, 0.4, 0.05)
        matrix.scale(0.003f, 0.003f, 0.003f)
        matrix.mulPose(Axis.ZP.rotationDegrees(183f))
        matrix.mulPose(Axis.YP.rotationDegrees(180f))
        matrix.mulPose(Axis.XP.rotationDegrees(-48.5f))
        matrix.mulPose(Axis.YP.rotationDegrees(-25f))

        drawPage(itemStack, 0, matrix, buffer, lightLevel)
        matrix.popPose()
      }
      run {
        matrix.pushPose() // Right hand, left page

        matrix.translate(-0.07f, 0.37f, 0f)
        matrix.scale(0.003f, 0.003f, 0.003f)
        matrix.mulPose(Axis.ZP.rotationDegrees(183f))
        matrix.mulPose(Axis.YP.rotationDegrees(180f))
        matrix.mulPose(Axis.XP.rotationDegrees(-49.75f))
        matrix.mulPose(Axis.YP.rotationDegrees(20f))

        drawPage(itemStack, 1, matrix, buffer, lightLevel)
        matrix.popPose()
      }
    } else {
      matrix.translate(0.31f, 0.0f, 0f)
      run {
        matrix.pushPose() // Left hand, left page

        matrix.translate(-0.31, 0.4, 0.05)
        matrix.scale(0.003f, 0.003f, 0.003f)
        matrix.mulPose(Axis.ZP.rotationDegrees(180f))
        matrix.mulPose(Axis.YP.rotationDegrees(180f))
        matrix.mulPose(Axis.XP.rotationDegrees(-48.5f))
        matrix.mulPose(Axis.YP.rotationDegrees(-25f))

        drawPage(itemStack, 0, matrix, buffer, lightLevel)
        matrix.popPose()
      }
      run {
        matrix.pushPose() // Left hand, left page

        matrix.translate(-0.07f, 0.38f, 0f)
        matrix.scale(0.003f, 0.003f, 0.003f)
        matrix.mulPose(Axis.ZP.rotationDegrees(183f))
        matrix.mulPose(Axis.YP.rotationDegrees(180f))
        matrix.mulPose(Axis.XP.rotationDegrees(-49.75f))
        matrix.mulPose(Axis.YP.rotationDegrees(20f))

        drawPage(itemStack, 1, matrix, buffer, lightLevel)
        matrix.popPose()
      }
    }

    matrix.popPose()
    return false
  }

  open fun drawPage(
    itemStack: ItemStack,
    page: Int,
    matrix: PoseStack?,
    buffer: MultiBufferSource,
    lightLevel: Int
  ) {
    if (matrix == null) return  // Prevents Sodium crash

    val font = Minecraft.getInstance().font
    val tag = itemStack.get(DataComponents.WRITTEN_BOOK_CONTENT)
    if (tag != null) {
      val pages = tag.pages()
      var sequence: MutableList<FormattedCharSequence?> = ArrayList()
      if (page >= pages.size) {
        val title = itemStack.get(ScriptorDataComponents.TOME_NAME)
        if (title != null) sequence.addAll(font.split(FormattedText.of(I18n.get(title)), 80))
        else sequence.addAll(font.split(FormattedText.of(tag.title()[true]), 80))
        sequence.addAll(font.split(FormattedText.of("By " + tag.author()), 80))
      } else sequence = font.split(FormattedText.of(LimitedBookSerializer.decodeText(tag)), 80)
      for (iter in sequence.indices) Minecraft.getInstance().font.drawInBatch(
        sequence[iter]!!,
        -17f,
        (-45 + 8 * iter).toFloat(),
        0x000000,
        false,
        matrix.last().pose(),
        buffer,
        Font.DisplayMode.NORMAL,
        0x0,
        lightLevel
      )
    }
  }
}
