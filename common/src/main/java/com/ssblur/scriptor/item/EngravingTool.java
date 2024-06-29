package com.ssblur.scriptor.item;

import com.ssblur.scriptor.events.network.ChalkNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EngravingTool extends Chalk {
  public EngravingTool(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
    if(level.isClientSide)
      ChalkNetwork.sendChalkMessage(true);

    return InteractionResultHolder.success(player.getItemInHand(interactionHand));
  }
}
