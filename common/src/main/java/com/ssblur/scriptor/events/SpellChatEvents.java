package com.ssblur.scriptor.events;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.damage.ScriptorDamage;
import com.ssblur.scriptor.effect.ScriptorEffects;
import com.ssblur.scriptor.helpers.ConfigHelper;
import com.ssblur.scriptor.helpers.DictionarySavedData;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.word.Spell;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.ChatEvent;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

public class SpellChatEvents implements ChatEvent.Received {
  @Override
  public EventResult received(@Nullable ServerPlayer player, Component component) {
    String sentence = component.getString();
    if(player != null) {
      var level = player.level();
      if (level instanceof ServerLevel server) {
        Spell spell = DictionarySavedData.computeIfAbsent(server).parse(sentence);
        if (spell != null) {
          if (player.hasEffect(ScriptorEffects.HOARSE.get())) {
            player.sendSystemMessage(Component.translatable("extra.scriptor.hoarse"));
            return EventResult.interruptFalse();
          } else if (player.hasEffect(ScriptorEffects.MUTE.get())) {
            player.sendSystemMessage(Component.translatable("extra.scriptor.mute"));
            return EventResult.interruptFalse();
          }
          spell.cast(new EntityTargetable(player));

          int cost = (int) Math.round(spell.cost() * 30);

          var config = ConfigHelper.getConfig();
          if (config.vocalCastingMaxCost >= 0 && cost > config.vocalCastingMaxCost)
            player.sendSystemMessage(Component.translatable("extra.scriptor.mute"));

          player.addEffect(new MobEffectInstance(ScriptorEffects.HOARSE.get(), cost));
          if (cost > config.vocalCastingHungerThreshold)
            player.addEffect(new MobEffectInstance(MobEffects.HUNGER, config.vocalCastingHungerThreshold));
          if (cost > config.vocalCastingHurtThreshold)
            player.hurt(Objects.requireNonNull(ScriptorDamage.overload(player)), (cost - config.vocalCastingHurtThreshold * 0.75f) / 100f);

          return EventResult.interruptFalse();
        }
      }
    }
    return EventResult.pass();
  }
}
