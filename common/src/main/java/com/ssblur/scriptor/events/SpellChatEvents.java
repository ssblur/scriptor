package com.ssblur.scriptor.events;

import com.ssblur.scriptor.ScriptorGameRules;
import com.ssblur.scriptor.damage.ScriptorDamage;
import com.ssblur.scriptor.data.DictionarySavedData;
import com.ssblur.scriptor.effect.ScriptorEffects;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.word.Spell;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.ChatEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import org.jetbrains.annotations.Nullable;

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

          int cost = (int) Math.round(spell.cost() * 30);

          if (level.getGameRules().getInt(ScriptorGameRules.VOCAL_MAX_COST) >= 0 && cost > level.getGameRules().getInt(ScriptorGameRules.VOCAL_MAX_COST))
            player.sendSystemMessage(Component.translatable("extra.scriptor.mute"));
		
		  double adjustedCost = (int) Math.round( (double)cost * ( (double) level.getGameRules().getInt(ScriptorGameRules.VOCAL_COOLDOWN_MULTIPLIER) / (double) 100 ) );
		  if (!player.isCreative()) {
			  player.addEffect(new MobEffectInstance(ScriptorEffects.HOARSE.get(), adjustedCost));
			  if (adjustedCost > level.getGameRules().getInt(ScriptorGameRules.VOCAL_HUNGER_THRESHOLD))
				player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 2*(adjustedCost - level.getGameRules().getInt(ScriptorGameRules.VOCAL_HUNGER_THRESHOLD))));
			  if (adjustedCost > level.getGameRules().getInt(ScriptorGameRules.VOCAL_DAMAGE_THRESHOLD))
				player.hurt(Objects.requireNonNull(ScriptorDamage.overload(player)), (adjustedCost - level.getGameRules().getInt(ScriptorGameRules.VOCAL_DAMAGE_THRESHOLD) * 0.75f) / 100f);
		  }
          if(player.getHealth() > 0)
            spell.cast(new EntityTargetable(player));

          return EventResult.interruptFalse();
        }
      }
    }
    return EventResult.pass();
  }
}
