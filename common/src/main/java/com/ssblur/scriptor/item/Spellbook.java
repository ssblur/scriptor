package com.ssblur.scriptor.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.advancement.ScriptorAdvancements;
import com.ssblur.scriptor.helpers.ComponentHelper;
import com.ssblur.scriptor.helpers.ConfigHelper;
import com.ssblur.scriptor.data.DictionarySavedData;
import com.ssblur.scriptor.helpers.LimitedBookSerializer;
import com.ssblur.scriptor.helpers.targetable.SpellbookTargetable;
import com.ssblur.scriptor.item.interfaces.ItemWithCustomRenderer;
import com.ssblur.scriptor.events.messages.EnchantNetwork;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Spellbook extends WrittenBookItem implements ItemWithCustomRenderer {

  public Spellbook(Properties properties) {
    super(properties);
  }

  @Override
  public Component getName(ItemStack itemStack) {
    CompoundTag compoundTag = itemStack.getTag();
    if (compoundTag != null) {
      String string = compoundTag.getString("title");
      if (!StringUtil.isNullOrEmpty(string)) {
        return Component.literal(string);
      }
    }
    return super.getName(itemStack);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
    var result = super.use(level, player, interactionHand);

    Tag tag = player.getItemInHand(interactionHand).getTag();
    if(tag instanceof CompoundTag compound && level instanceof ServerLevel server) {
      var text = compound.getList("pages", Tag.TAG_STRING);
      Spell spell = DictionarySavedData.computeIfAbsent(server).parse(LimitedBookSerializer.decodeText(text));
      if(spell != null) {
        var config = ConfigHelper.getConfig();
        if(spell.cost() > config.basicTomeMaxCost) {
          player.sendSystemMessage(Component.translatable("extra.scriptor.fizzle"));
          ScriptorAdvancements.FIZZLE.trigger((ServerPlayer) player);
          if(!player.isCreative())
            player.getCooldowns().addCooldown(this, 350);
          return result;
        }
        spell.cast(new SpellbookTargetable(player.getItemInHand(interactionHand), player, player.getInventory().selected).withTargetItem(false));
        if(!player.isCreative())
          player.getCooldowns().addCooldown(this, (int) Math.round(spell.cost() * 7));
        return InteractionResultHolder.fail(player.getItemInHand(interactionHand));
      }
    }

    return result;
  }

  public boolean overrideStackedOnOther(ItemStack itemStack, Slot slot, ClickAction clickAction, Player player) {
    if (clickAction == ClickAction.SECONDARY && !slot.getItem().isEmpty()) {
      if(player.getCooldowns().isOnCooldown(this)) return true;
      var level = player.level();
      if(!level.isClientSide) return true;

      if(player.isCreative())
        EnchantNetwork.clientUseBookCreative(itemStack, slot.index);
      else
        EnchantNetwork.clientUseBook(slot.index);
      return true;
    }
    return false;
  }

  @Override
  public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
    super.appendHoverText(itemStack, level, list, tooltipFlag);

    if(itemStack.getTag() != null && itemStack.getTag().getCompound("scriptor") != null) {
      CompoundTag tag = itemStack.getTag();

      var scriptor = tag.getCompound("scriptor");
      if(scriptor.contains("identified")) {
        if(Screen.hasShiftDown())
          for(var key: scriptor.getCompound("identified").getAllKeys()) {
            String[] parts = key.split(":", 2);
            if(parts.length == 2)
              ComponentHelper.updateTooltipWith(list,parts[0] + ".scriptor." + parts[1]);
            else
              ScriptorMod.LOGGER.error("Invalid Identify entry: " + key);
          }
        else
          list.add(Component.translatable("extra.scriptor.tome_identified"));
      }
    }
  }

  @Override
  public boolean render(
    AbstractClientPlayer player,
    float i,
    float pitch,
    InteractionHand hand,
    float swingProgress,
    ItemStack itemStack,
    float readyProgress,
    PoseStack matrix,
    MultiBufferSource buffer,
    int lightLevel
  ) {
    matrix.pushPose();
    int v = hand == InteractionHand.MAIN_HAND ? 1 : -1;
    matrix.translate((float)v * 0.56f, -0.52f + readyProgress * -0.6f, -0.72f);
    float g = Mth.sin(swingProgress * swingProgress * (float)Math.PI);
    matrix.mulPose(Axis.YP.rotationDegrees(i * (45.0f + g * -20.0f)));
    float h = Mth.sin(Mth.sqrt(swingProgress) * (float)Math.PI);
    matrix.mulPose(Axis.ZP.rotationDegrees(i * h * -20.0f));
    matrix.mulPose(Axis.XP.rotationDegrees(h * -80.0f));
    matrix.mulPose(Axis.YP.rotationDegrees(i * -45.0f));

    if(hand == InteractionHand.MAIN_HAND) {
      {
        matrix.pushPose(); // Right hand, left page

        matrix.translate(-0.30f, 0.4f, 0.05);
        matrix.scale(0.003f, 0.003f, 0.003f);
        matrix.mulPose(Axis.ZP.rotationDegrees(183f));
        matrix.mulPose(Axis.YP.rotationDegrees(180));
        matrix.mulPose(Axis.XP.rotationDegrees(-48.5f));
        matrix.mulPose(Axis.YP.rotationDegrees(-25f));

        drawPage(itemStack, 0, matrix, buffer, lightLevel);

        matrix.popPose();
      }
      {
        matrix.pushPose(); // Right hand, left page

        matrix.translate(-0.07f, 0.37f, 0);
        matrix.scale(0.003f, 0.003f, 0.003f);
        matrix.mulPose(Axis.ZP.rotationDegrees(183f));
        matrix.mulPose(Axis.YP.rotationDegrees(180));
        matrix.mulPose(Axis.XP.rotationDegrees(-49.75f));
        matrix.mulPose(Axis.YP.rotationDegrees(20f));

        drawPage(itemStack, 1, matrix, buffer, lightLevel);

        matrix.popPose();
      }
    } else {
      matrix.translate(0.31f, 0.0f, 0);
      {
        matrix.pushPose(); // Left hand, left page

        matrix.translate(-0.31f, 0.4f, 0.05);
        matrix.scale(0.003f, 0.003f, 0.003f);
        matrix.mulPose(Axis.ZP.rotationDegrees(180f));
        matrix.mulPose(Axis.YP.rotationDegrees(180));
        matrix.mulPose(Axis.XP.rotationDegrees(-48.5f));
        matrix.mulPose(Axis.YP.rotationDegrees(-25f));

        drawPage(itemStack, 0, matrix, buffer, lightLevel);

        matrix.popPose();
      }
      {
        matrix.pushPose(); // Left hand, left page

        matrix.translate(-0.07f, 0.38f, 0);
        matrix.scale(0.003f, 0.003f, 0.003f);
        matrix.mulPose(Axis.ZP.rotationDegrees(183f));
        matrix.mulPose(Axis.YP.rotationDegrees(180));
        matrix.mulPose(Axis.XP.rotationDegrees(-49.75f));
        matrix.mulPose(Axis.YP.rotationDegrees(20f));

        drawPage(itemStack, 1, matrix, buffer, lightLevel);

        matrix.popPose();
      }
    }

    matrix.popPose();
    return false;
  }

  public void drawPage(ItemStack itemStack, int page, PoseStack matrix, MultiBufferSource buffer, int lightLevel) {
    var font = Minecraft.getInstance().font;
    var tag = itemStack.getTag();
    if(tag != null && tag.contains("pages")) {
      var pages = tag.getList("pages", Tag.TAG_STRING);
      List<FormattedCharSequence> sequence = new ArrayList<>();
      if (page >= pages.size()) {
        if (tag.contains("title"))
          sequence.addAll(font.split(FormattedText.of(tag.getString("title")), 80));
        if (tag.contains("author"))
          sequence.addAll(font.split(FormattedText.of("By " + tag.getString("author")), 80));
      } else
        sequence = font.split(FormattedText.of(LimitedBookSerializer.decodeText(pages.getString(page))), 80);
      for (int iter = 0; iter < sequence.size(); iter++)
        Minecraft.getInstance().font.drawInBatch(
          sequence.get(iter),
          -17,
          -45 + 8 * iter,
          0x000000,
          false,
          matrix.last().pose(),
          buffer,
          Font.DisplayMode.NORMAL,
          0x0,
          lightLevel
        );
    }
  }

  public boolean isFoil(ItemStack itemStack) {
    return itemStack.isEnchanted();
  }
}
