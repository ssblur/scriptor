package com.ssblur.scriptor.events;

import com.ssblur.scriptor.helpers.DictionarySavedData;
import com.ssblur.scriptor.word.Spell;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.ChatEvent;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class SpellChatEvents implements ChatEvent.Received {
  @Override
  public EventResult received(@Nullable ServerPlayer player, Component component) {
    String sentence = component.getString();
    if(player.level instanceof ServerLevel server) {
      Spell spell = DictionarySavedData.computeIfAbsent(server).parse(sentence);
      if (spell != null) {
        spell.cast(player);

        return EventResult.interruptFalse();
      }
    }
    return EventResult.pass();
  }
}
