package com.ssblur.scriptor.events;

import com.ssblur.scriptor.ScriptorDamage;
import com.ssblur.scriptor.config.ChatRules;
import com.ssblur.scriptor.config.ScriptorGameRules;
import com.ssblur.scriptor.data.saved_data.DictionarySavedData;
import com.ssblur.scriptor.effect.EmpoweredStatusEffect;
import com.ssblur.scriptor.effect.ScriptorEffects;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.word.Spell;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.ChatEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
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
          if (player.hasEffect(ScriptorEffects.INSTANCE.getHOARSE())) {
            player.sendSystemMessage(Component.translatable("extra.scriptor.hoarse"));
            return EventResult.interruptFalse();
          } else if (player.hasEffect(ScriptorEffects.INSTANCE.getMUTE())) {
            player.sendSystemMessage(Component.translatable("extra.scriptor.mute"));
            return EventResult.interruptFalse();
          }

          int cost = (int) Math.round(spell.cost() * 30);
          float costScale = 1.0f;
          for(var instance: player.getActiveEffects())
            if(instance.getEffect().value() instanceof EmpoweredStatusEffect empoweredStatusEffect)
              for(int i = 0; i <= instance.getAmplifier(); i++)
                costScale *= empoweredStatusEffect.getScale();
          cost = Math.round(((float) cost) * costScale);

          if (level.getGameRules().getInt(ScriptorGameRules.INSTANCE.getVOCAL_MAX_COST()) >= 0 && cost > level.getGameRules().getInt(ScriptorGameRules.INSTANCE.getVOCAL_MAX_COST()))
            player.sendSystemMessage(Component.translatable("extra.scriptor.mute"));
		
		  int adjustedCost = (int) Math.round( (double)cost * ( (double) level.getGameRules().getInt(ScriptorGameRules.INSTANCE.getVOCAL_COOLDOWN_MULTIPLIER()) / (double) 100 ) );
		  if (!player.isCreative()) {
        player.addEffect(new MobEffectInstance(ScriptorEffects.INSTANCE.getHOARSE().ref(), adjustedCost));
			  if (adjustedCost > level.getGameRules().getInt(ScriptorGameRules.INSTANCE.getVOCAL_HUNGER_THRESHOLD()))
				  player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 2*(adjustedCost - level.getGameRules().getInt(ScriptorGameRules.INSTANCE.getVOCAL_HUNGER_THRESHOLD()))));
			  if (adjustedCost > level.getGameRules().getInt(ScriptorGameRules.INSTANCE.getVOCAL_DAMAGE_THRESHOLD()))
				  player.hurt(Objects.requireNonNull(ScriptorDamage.overload(player)), (adjustedCost - level.getGameRules().getInt(ScriptorGameRules.INSTANCE.getVOCAL_DAMAGE_THRESHOLD()) * 0.75f) / 100f);
		  }
        if(player.getHealth() > 0)
          spell.cast(new EntityTargetable(player));

        if(!server.getGameRules().getBoolean(ChatRules.INSTANCE.getSHOW_SPELLS_IN_CHAT()))
          return EventResult.interruptFalse();
        }
      }

      if (level instanceof ServerLevel server && server.getGameRules().getBoolean(ChatRules.INSTANCE.getPROXIMITY_CHAT())) {
        int distance = server.getGameRules().getInt(ChatRules.INSTANCE.getPROXIMITY_RANGE());
        var name = player.getDisplayName();
        Component message;
        if(name == null)
          message = Component.literal("> ").append(component);
        else
          message = Component.literal("<")
            .append(name)
            .append(Component.literal("> "))
            .append(component);

        var players = server.getPlayers(recipient -> recipient.distanceTo(player) <= distance);
        for(var recipient: players)
          recipient.sendSystemMessage(message);

        if(players.size() <= 1)
          player.sendSystemMessage(Component.translatable("command.scriptor.unheard")
            .withStyle(ChatFormatting.GRAY)
            .withStyle(ChatFormatting.ITALIC));

        return EventResult.interruptFalse();
      }
    }

    return EventResult.pass();
  }
}
