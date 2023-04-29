package com.ssblur.scriptor.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.ssblur.scriptor.events.messages.EnchantNetwork;
import com.ssblur.scriptor.helpers.ComponentHelper;
import com.ssblur.scriptor.helpers.DictionarySavedData;
import com.ssblur.scriptor.helpers.LimitedBookSerializer;
import com.ssblur.scriptor.helpers.targetable.SpellbookTargetable;
import com.ssblur.scriptor.item.interfaces.ItemWithCustomRenderer;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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
