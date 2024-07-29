package com.ssblur.scriptor.item;

import com.ssblur.scriptor.events.network.server.ChalkNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class Chalk extends Item {
  public Chalk(Properties properties) {
    super(properties);
  }

  @Override
  public void appendHoverText(ItemStack itemStack, TooltipContext level, List<Component> list, TooltipFlag tooltipFlag) {
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
    var name = itemStack.get(DataComponents.ITEM_NAME);
    return name != null;
  }
}
