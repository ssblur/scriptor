package com.ssblur.scriptor.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AncientSpellbook extends Item {
  public AncientSpellbook(Properties properties) {
    super(properties.stacksTo(1));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
    var result = super.use(level, player, interactionHand);

    if(!level.isClientSide) {
      player.sendSystemMessage(Component.translatable("extra.scriptor.tome_use"));
      player.getCooldowns().addCooldown(this, 20);
      return InteractionResultHolder.consume(player.getItemInHand(interactionHand));
    }

    return result;
  }
}
