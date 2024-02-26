package com.ssblur.scriptor.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WritableBookItem;
import net.minecraft.world.level.Level;

public class WritableSpellbook extends WritableBookItem {
  public WritableSpellbook(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
    var itemStack = player.getItemInHand(interactionHand);
    if(level.isClientSide)
      openScreen(player, itemStack, interactionHand);
    return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
  }

  @Environment(EnvType.CLIENT)
  public void openScreen(Player player, ItemStack itemStack, InteractionHand interactionHand) {
    Minecraft.getInstance().setScreen(new BookEditScreen(player, itemStack, interactionHand));
  }
}
