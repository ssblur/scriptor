package com.ssblur.scriptor.helpers;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.item.ScriptorItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LimitedBookSerializer {
  static Type PAGE_TYPE = new TypeToken<Page>() {}.getType();
  static class Page {
    String text;
    public Page(String text) {
      this.text = text;
    }
  }

  /**
   * Transform a book's pages ListTag into a single String for processing.
   * @param text A book's pages ListTag
   * @return A single string, with no spaces between pages.
   */
  public static String decodeText(ListTag text) {
    Gson gson = new Gson();
    StringBuilder builder = new StringBuilder();
    for(Tag tag: text) {
      try {
        Page page = gson.fromJson(tag.getAsString(), PAGE_TYPE);
        builder.append(page.text.strip());
        builder.append(" ");
        continue;
      } catch(Exception ignored) {}

      try {
        var string = gson.fromJson(tag.getAsString(), String.class);
        builder.append(string.strip());
        builder.append(" ");
      } catch(Exception e) {
        builder.append(tag.toString());
        builder.append(" ");
      }
    }
    return builder.toString().stripTrailing();
  }

  public static String decodeText(String text) {
    Gson gson = new Gson();
    try {
      Page page = gson.fromJson(text, PAGE_TYPE);
      return page.text.strip();
    } catch(Exception ignored) {}

    try {
      var string = gson.fromJson(text, String.class);
      return string.strip();
    } catch(Exception e) {
      return "Error parsing text: \""
        + text
        + "\".";
    }
  }

  /**
   * A helper for encoding a String as a book-compatible JSON list.
   * @param text The text to encode.
   * @return An encoded JSON string
   */
  public static ListTag encodeText(String text) {
    Gson gson = new Gson();
    List<Page> list = new ArrayList<>();
    String[] tokens = text.split("\\s+");

    int pageLength = 0;
    StringBuilder page = new StringBuilder();
    for(var token: tokens) {
      if(token.length() >= 96) {
        pageLength = 0;
        list.add(new Page(page.toString()));
        list.add(new Page(token));
        page = new StringBuilder();
      } else if((token.length() + page.length()) >= 96) {
        pageLength = token.length();
        list.add(new Page(page.toString()));
        page = new StringBuilder();
        page.append(token);
        page.append(" ");
      } else {
        pageLength += token.length();
        page.append(token);
        page.append(" ");
      }
    }
    if(!page.isEmpty()) list.add(new Page(page.toString()));

    ListTag tag = new ListTag();
    for(Page p: list)
      tag.add(StringTag.valueOf(gson.toJson(p)));

    return tag;
  }

  public static ItemStack createSpellbook(String author, String title, String text) {
    CompoundTag tag = new CompoundTag();
    tag.putString("author", author);
    tag.putString("title", title);
    tag.put("pages", encodeText(text));

    ItemStack itemStack = new ItemStack(ScriptorItems.SPELLBOOK.get());
    itemStack.setCount(1);
    itemStack.setTag(tag);
    return itemStack;
  }
}
