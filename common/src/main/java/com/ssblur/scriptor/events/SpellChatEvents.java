package com.ssblur.scriptor.events;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.damage.OverloadDamageSource;
import com.ssblur.scriptor.effect.ScriptorEffects;
import com.ssblur.scriptor.helpers.DictionarySavedData;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.word.Spell;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.ChatEvent;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import org.jetbrains.annotations.Nullable;

public class SpellChatEvents implements ChatEvent.Received {
  @Override
  public EventResult received(@Nullable ServerPlayer player, Component component) {
    String sentence = component.getString();
    if(player != null && player.level instanceof ServerLevel server) {
      Spell spell = DictionarySavedData.computeIfAbsent(server).parse(sentence);
      if (spell != null) {
        if(player.hasEffect(ScriptorEffects.HOARSE.get())) {
          player.sendSystemMessage(Component.translatable("extra.scriptor.hoarse"));
          return EventResult.interruptFalse();
        } else if(player.hasEffect(ScriptorEffects.MUTE.get())) {
          player.sendSystemMessage(Component.translatable("extra.scriptor.mute"));
          return EventResult.interruptFalse();
        }
        spell.cast(new EntityTargetable(player));

        int cost = (int) Math.round(spell.cost() * 30);

        player.addEffect(new MobEffectInstance(ScriptorEffects.HOARSE.get(), cost));
        if(cost > 400)
          player.addEffect(new MobEffectInstance(MobEffects.HUNGER, cost - 300));
        if(cost > 1200)
          player.hurt(new OverloadDamageSource(), ((float) (cost - 800)) / 100f);

        return EventResult.interruptFalse();
      }
    }
    return EventResult.pass();
  }
}
