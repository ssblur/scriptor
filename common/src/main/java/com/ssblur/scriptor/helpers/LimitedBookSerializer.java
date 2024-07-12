package com.ssblur.scriptor.helpers;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.data_components.ScriptorDataComponents;
import com.ssblur.scriptor.item.ScriptorItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.network.Filterable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WrittenBookContent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class LimitedBookSerializer {
  public static String decodeText(WrittenBookContent text) {
    var pages = text.getPages(true);
    StringBuilder builder = new StringBuilder();
    for(Component component: pages) {
      builder.append(component.getString().stripTrailing());
      builder.append(" ");
    }
    return builder.toString().stripTrailing();
  }

  /**
   * A helper for encoding a String as a book-compatible JSON list.
   * @param text The text to encode.
   * @return An encoded JSON string
   */
  public static List<Filterable<Component>> encodeText(String text) {
    String[] tokens = text.split("\\s+");
    List<Filterable<Component>> list = new ArrayList<>();

    int pageLength = 0;
    StringBuilder page = new StringBuilder();
    for(var token: tokens) {
      if(token.length() >= 96) {
        pageLength = 0;
        list.add(filterable(page.toString()));
        list.add(filterable(token));
        page = new StringBuilder();
      } else if((token.length() + page.length()) >= 96) {
        pageLength = token.length();
        list.add(filterable(page.toString()));
        page = new StringBuilder();
        page.append(token);
        page.append(" ");
      } else {
        pageLength += token.length();
        page.append(token);
        page.append(" ");
      }
    }
    if(!page.isEmpty()) list.add(filterable(page.toString()));

    return list;
  }

  public static ItemStack createSpellbook(String author, String title, String text, @Nullable String item) {
    ItemStack itemStack;
    if(item != null)
      itemStack = new ItemStack(Objects.requireNonNull(ScriptorItems.ITEMS.getRegistrar().get(new ResourceLocation(item))));
    else
      itemStack = new ItemStack(ScriptorItems.SPELLBOOK.get());
    itemStack.setCount(1);
    itemStack.set(
      DataComponents.WRITTEN_BOOK_CONTENT,
      new WrittenBookContent(new Filterable<>("Spellbook", Optional.of("spellbook")), author, 0, encodeText(text), false)
    );
    itemStack.set(ScriptorDataComponents.TOME_NAME, title);
    if(ScriptorMod.COMMUNITY_MODE)
      itemStack.set(ScriptorDataComponents.COMMUNITY_MODE, true);

    return itemStack;
  }

  static Filterable<Component> filterable(String page) {
    return Filterable.passThrough(Component.literal(page));
  }
}
