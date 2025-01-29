package com.ssblur.scriptor.item.books

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.network.chat.FormattedText
import net.minecraft.world.item.ItemStack

class ObfuscatedSpellbook(properties: Properties) : Spellbook(properties) {
    override fun drawPage(
        itemStack: ItemStack,
        page: Int,
        matrix: PoseStack?,
        buffer: MultiBufferSource,
        lightLevel: Int
    ) {
        if (matrix == null) return
        val font = Minecraft.getInstance().font
        val sequence = font.split(FormattedText.of("§k" + "*".repeat(400)), 80)
        for (iter in sequence.indices) Minecraft.getInstance().font.drawInBatch(
            sequence[iter],
            -17f,
            (-45 + 8 * iter).toFloat(),
            0x999999,
            false,
            matrix.last().pose(),
            buffer,
            Font.DisplayMode.NORMAL,
            0x0,
            lightLevel
        )
    }
}
