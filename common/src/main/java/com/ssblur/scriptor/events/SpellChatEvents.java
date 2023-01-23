package com.ssblur.scriptor.events;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.helpers.DictionarySavedData;
import com.ssblur.scriptor.word.Spell;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.ChatEvent;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import org.jetbrains.annotations.Nullable;

public class SpellChatEvents implements ChatEvent.Received {
  @Override
  public EventResult received(@Nullable ServerPlayer player, Component component) {
    String sentence = component.getString();
    if(player != null && player.level instanceof ServerLevel server) {
      Spell spell = DictionarySavedData.computeIfAbsent(server).parse(sentence);
      if (spell != null) {
        if(player.hasEffect(ScriptorMod.HOARSE.get()) || player.hasEffect(ScriptorMod.MUTE.get()))
          return EventResult.interruptFalse();
        spell.cast(player);

        int cost = spell.cost() * 30;
        MobEffectInstance instance = new MobEffectInstance(ScriptorMod.HOARSE.get(), cost);
        player.addEffect(instance);

        return EventResult.interruptFalse();
      }
    }
    return EventResult.pass();
  }
}
