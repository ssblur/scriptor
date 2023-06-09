package com.ssblur.scriptor.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ObfuscatedSpellbook extends Spellbook {

  public ObfuscatedSpellbook(Properties properties) {
    super(properties);
  }

  public void drawPage(ItemStack itemStack, int page, PoseStack matrix, MultiBufferSource buffer, int lightLevel) {
    var font = Minecraft.getInstance().font;
    List<FormattedCharSequence> sequence = font.split(FormattedText.of("§k" + "*".repeat(400)), 80);
    for (int iter = 0; iter < sequence.size(); iter++)
      Minecraft.getInstance().font.drawInBatch(
        sequence.get(iter),
        -17,
        -45 + 8 * iter,
        0x999999,
        false,
        matrix.last().pose(),
        buffer,
        false,
        0x0,
        lightLevel
      );
  }
}
