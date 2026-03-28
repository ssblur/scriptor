package com.ssblur.scriptor.events

import com.mojang.blaze3d.systems.RenderSystem
import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.extension.EntityCastCooldownExtension.castCooldown
import com.ssblur.unfocused.event.client.ClientGuiRenderEvent
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import kotlin.math.max
import kotlin.math.roundToInt

object ScriptorCooldownHud {
  private var fadeout = 0.0
  private var fullBar = 0.0
  val BACKGROUND = ScriptorMod.location("hud/cooldown_bar_background")
  val FOREGROUND = ScriptorMod.location("hud/cooldown_bar_progress")
  const val EMPTY_TICKS = 30.0
  const val FADEOUT_START = 0.0

  fun render(guiGraphics: GuiGraphics, delta: DeltaTracker) {
    val player = Minecraft.getInstance().player ?: return
    if(player.castCooldown > 0) {
      fadeout = EMPTY_TICKS
      fullBar = max(player.castCooldown.toDouble(), fullBar)
    } else {
      fullBar = 0.0
    }

    if(fadeout <= 0) {
      return
    }
    if(!Minecraft.getInstance().isPaused) fadeout -= delta.gameTimeDeltaTicks

    val barPortion = if(fullBar > 0.0) player.castCooldown / fullBar else 0.0
    val barTransparency = 1.0f
//    val barTransparency = (fadeout / FADEOUT_START).coerceIn(0.0..1.0)
    val y = guiGraphics.guiHeight() - 29
    val w = 182
    val x = (guiGraphics.guiWidth() - w) / 2
    val bw = (w * barPortion).roundToInt()
    val h = 5

    RenderSystem.enableBlend()
    guiGraphics.setColor(1.0f, 1.0f, 1.0f, barTransparency.toFloat())
    guiGraphics.blitSprite(BACKGROUND, x, y, w, h)
    guiGraphics.enableScissor(x, y, x + bw, y + h)
    guiGraphics.setColor(1.0f, 1.0f, 1.0f, barTransparency.toFloat())
    guiGraphics.blitSprite(FOREGROUND, x, y, w, h)
    guiGraphics.disableScissor()

    if(player.experienceLevel > 0) {
      guiGraphics.setColor(1.0f, 1.0f, 1.0f, barTransparency.toFloat())
      val font = Minecraft.getInstance().font
      val s = player.experienceLevel.toString()
      val lx = (guiGraphics.guiWidth() - font.width(s)) / 2
      val ly = guiGraphics.guiHeight() - 35
      guiGraphics.drawString(font, s, lx + 1, ly, 0, false)
      guiGraphics.drawString(font, s, lx - 1, ly, 0, false)
      guiGraphics.drawString(font, s, lx, ly + 1, 0, false)
      guiGraphics.drawString(font, s, lx, ly - 1, 0, false)
      guiGraphics.drawString(font, s, lx, ly, 0xff9979b3u.toInt(), false)
    }

    guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f)
  }

  fun shouldNotRenderXpBar(): Boolean {
    Minecraft.getInstance().player?.let {
      if(it.castCooldown > 0 || fadeout > 0) return true
    }
    return false
  }

  fun init() {
    ClientGuiRenderEvent.register{ (guiGraphics, delta) ->
      render(guiGraphics, delta)
    }
  }
}