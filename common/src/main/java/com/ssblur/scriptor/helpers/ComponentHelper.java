package com.ssblur.scriptor.helpers;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.ArrayList;
import java.util.List;

public class ComponentHelper {
  static final int DEFAULT_WIDTH = 38;

  public static void updateTooltipWith(int size, List<Component> list, String key, Object... substitutions) {
    updateTooltipWith(size, Style.EMPTY, list, key, substitutions);
  }

  public static void updateTooltipWith(List<Component> list, String key, Object... substitutions) {
    updateTooltipWith(DEFAULT_WIDTH, list, key, substitutions);
  }

  public static void updateTooltipWith(ChatFormatting style, List<Component> list, String key, Object... substitutions) {
    updateTooltipWith(DEFAULT_WIDTH, style, list, key, substitutions);
  }

  public static void updateTooltipWith(Style style, List<Component> list, String key, Object... substitutions) {
    updateTooltipWith(DEFAULT_WIDTH, style, list, key, substitutions);
  }

  public static void updateTooltipWith(int size, Style style, List<Component> list, String key, Object... substitutions) {
    list.addAll(orderedTranslatableComponents(size, key, substitutions).stream().map(component -> component.withStyle(style)).toList());
  }

  public static void updateTooltipWith(int size, ChatFormatting style, List<Component> list, String key, Object... substitutions) {
    list.addAll(orderedTranslatableComponents(size, key, substitutions).stream().map(component -> component.withStyle(style)).toList());
  }

  public static List<MutableComponent> orderedTranslatableComponents(int size, String key, Object... substitutions) {
    var text = I18n.get(key, substitutions);
    var tokens = text.split("\\s+");
    ArrayList<String> list = new ArrayList<>();
    var line = new StringBuilder();
    for(var token: tokens) {
      if(token.length() >= size) {
        list.add(line.toString());
        list.add(token);
        line = new StringBuilder();
      } else if((token.length() + line.length()) >= size) {
        list.add(line.toString());
        line = new StringBuilder();
        line.append(token);
        line.append(" ");
      } else {
        line.append(token);
        line.append(" ");
      }
    }
    list.add(line.toString());

    return list.stream().map(Component::literal).toList();
  }
}
