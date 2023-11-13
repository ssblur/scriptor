package com.ssblur.scriptor.item;

import com.ssblur.scriptor.advancement.ScriptorAdvancements;
import com.ssblur.scriptor.data.DictionarySavedData;
import com.ssblur.scriptor.events.messages.ChalkNetwork;
import com.ssblur.scriptor.events.reloadlisteners.TomeReloadListener;
import com.ssblur.scriptor.helpers.LimitedBookSerializer;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Chalk extends Item {
  public Chalk(Properties properties) {
    super(properties);
  }

  @Override
  public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
    super.appendHoverText(itemStack, level, list, tooltipFlag);
    if(!hasName(itemStack)) {
      list.add(Component.translatable("extra.scriptor.chalk_unnamed_1").withStyle(ChatFormatting.RED));
      list.add(Component.translatable("extra.scriptor.chalk_unnamed_2"));
    }
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
    var result = super.use(level, player, interactionHand);

    if(level.isClientSide)
      ChalkNetwork.sendChalkMessage();

    return result;
  }

  public static boolean hasName(ItemStack itemStack) {
    if(itemStack.hasTag()) {
      var display = itemStack.getTagElement("display");
      return display != null && display.getString("Name") != null && !display.getString("Name").isEmpty();
    }
    return false;
  }
}
