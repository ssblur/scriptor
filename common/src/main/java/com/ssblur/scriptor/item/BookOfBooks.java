package com.ssblur.scriptor.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ssblur.scriptor.data_components.BookOfBooksData;
import com.ssblur.scriptor.data_components.ScriptorDataComponents;
import com.ssblur.scriptor.helpers.SpellbookHelper;
import com.ssblur.scriptor.item.interfaces.ItemWithCustomRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class BookOfBooks extends Item implements ItemWithCustomRenderer {
  int capacity;
  public BookOfBooks(Properties properties, int capacity) {
    super(properties);
    this.capacity = capacity;
    SpellbookHelper.SPELLBOOKS.add(this);
  }

  @Override
  public boolean overrideOtherStackedOnMe(ItemStack book, ItemStack itemStack, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
    if (clickAction != ClickAction.SECONDARY || !slot.allowModification(player))
      return false;

    if(itemStack.isEmpty())
      remove(book, slotAccess);
    else if(itemStack.getItem() instanceof Spellbook && getInventory(book).size() < capacity)
      add(book, itemStack);
    else
      return false;

    return true;
  }

  @Override
  public Component getName(ItemStack itemStack) {
    var item = getActiveItem(itemStack);
    if(!item.isEmpty())
      return Component.translatable("item.scriptor.book_of_books_2", item.getHoverName().getString());

    return super.getName(itemStack);
  }

  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
    var result = super.use(level, player, interactionHand);

    ItemStack book = player.getItemInHand(interactionHand);
    boolean castResult = SpellbookHelper.castFromItem(getActiveItem(book), player);

    if(castResult)
      return result;
    return InteractionResultHolder.fail(player.getItemInHand(interactionHand));
  }

  @Override
  public void appendHoverText(ItemStack itemStack, TooltipContext level, List<Component> list, TooltipFlag tooltipFlag) {
    super.appendHoverText(itemStack, level, list, tooltipFlag);

    var inventory = getInventory(itemStack);

    if(inventory.size() == 0) {
      list.add(Component.translatable("lore.scriptor.empty_book_1").withStyle(ChatFormatting.GRAY));
      list.add(Component.translatable("lore.scriptor.empty_book_2").withStyle(ChatFormatting.GRAY));
    }

    for(int i = 0; i < inventory.size(); i++)
      if(i == getActiveSlot(itemStack))
        list.add(Component.translatable("lore.scriptor.active_book", inventory.get(i).getHoverName().getString()).withStyle(ChatFormatting.GREEN));
      else
        list.add(Component.literal(inventory.get(i).getHoverName().getString()).withStyle(ChatFormatting.GRAY));

    list.add(Component.translatable("lore.scriptor.book_scroll").withStyle(ChatFormatting.GRAY));
  }

  static List<ItemStack> getInventory(ItemStack book) {
    var data = book.get(ScriptorDataComponents.BOOK_OF_BOOKS);

    if(data == null)
      return List.of();

    return data.items();
  }

  static int getActiveSlot(ItemStack book) {
    var data = book.get(ScriptorDataComponents.BOOK_OF_BOOKS);
    if(data == null) return 0;
    return data.active();
  }

  static ItemStack getActiveItem(ItemStack book) {
    int slot = getActiveSlot(book);
    var inventory = getInventory(book);
    if(inventory.isEmpty()) return ItemStack.EMPTY;
    if(inventory.size() <= slot) return inventory.get(inventory.size() - 1);
    return inventory.get(slot);
  }

  static void remove(ItemStack book, SlotAccess slotAccess) {
    if(!slotAccess.get().isEmpty()) return;

    var data = book.get(ScriptorDataComponents.BOOK_OF_BOOKS);
    if(data == null) return;

    var list = new ArrayList<>(data.items());
    if(list.isEmpty()) return;

    var item = list.remove(list.size() - 1);
    book.set(ScriptorDataComponents.BOOK_OF_BOOKS, new BookOfBooksData(list, data.active()));
    slotAccess.set(item);
  }

  static void add(ItemStack book, ItemStack item) {
    if(item.isEmpty()) return;
    var insert = item.copy();
    item.shrink(1);

    var data = book.get(ScriptorDataComponents.BOOK_OF_BOOKS);
    if(data == null)
      data = new BookOfBooksData(List.of(), 0);

    var list = new ArrayList<>(data.items());

    list.add(insert);
    book.set(ScriptorDataComponents.BOOK_OF_BOOKS, new BookOfBooksData(list, data.active()));
  }

  @Override
  public boolean render(AbstractClientPlayer player, float i, float pitch, InteractionHand hand, float swingProgress, ItemStack itemStack, float readyProgress, PoseStack matrix, MultiBufferSource buffer, int lightLevel) {
    if(getActiveItem(player.getItemInHand(hand)).getItem() instanceof ItemWithCustomRenderer renderer)
      return renderer.render(player, i, pitch, hand, swingProgress, getActiveItem(player.getItemInHand(hand)), readyProgress, matrix, buffer, lightLevel);
    return false;
  }
}
