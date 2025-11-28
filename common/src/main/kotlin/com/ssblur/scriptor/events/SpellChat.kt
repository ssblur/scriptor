package com.ssblur.scriptor.events

import com.ssblur.scriptor.ScriptorDamage.overload
import com.ssblur.scriptor.config.ScriptorConfig
import com.ssblur.scriptor.data.saved_data.DictionarySavedData.Companion.computeIfAbsent
import com.ssblur.scriptor.effect.EmpoweredStatusEffect
import com.ssblur.scriptor.effect.ScriptorEffects.HOARSE
import com.ssblur.scriptor.effect.ScriptorEffects.MUTE
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.unfocused.event.common.PlayerChatEvent
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.player.Player
import java.util.function.Predicate
import kotlin.math.roundToInt

object SpellChat {
  init {
    PlayerChatEvent.Before.register {
      val player = it.player
      val component = it.message
      val sentence = component.string
      val level = player.level()
      if (level is ServerLevel)
        if (castFromChat(player, level, sentence)) return@register it.cancel()

      if (level is ServerLevel && ScriptorConfig.PROXIMITY_CHAT()) {
        val distance = ScriptorConfig.PROXIMITY_RANGE()
        val name = player.displayName
        val message: Component =
          if (name == null) Component.literal("> ").append(component)
          else Component.literal("<")
            .append(name)
            .append(Component.literal("> "))
            .append(component)

        val players: List<ServerPlayer> =
          level.getPlayers(Predicate { recipient: ServerPlayer -> recipient.distanceTo(player) <= distance })
        for (recipient in players) recipient.sendSystemMessage(message)

        if (players.size <= 1) player.sendSystemMessage(
          Component.translatable("command.scriptor.unheard")
            .withStyle(ChatFormatting.GRAY)
            .withStyle(ChatFormatting.ITALIC)
        )

        return@register it.cancel()
      }
    }
  }

  fun castFromChat(player: Player, level: ServerLevel, sentence: String): Boolean {
    val spell = computeIfAbsent(level).parse(sentence)
    if (spell != null) {
      if (player.hasEffect(HOARSE.ref())) {
        player.sendSystemMessage(Component.translatable("extra.scriptor.hoarse"))
        return true
      } else if (player.hasEffect(MUTE.ref())) {
        player.sendSystemMessage(Component.translatable("extra.scriptor.mute"))
        return true
      }

      var cost = (spell.cost() * 30).roundToInt()
      var costScale = 1.0f
      for (instance in player.activeEffects)
        if (instance.effect.value() is EmpoweredStatusEffect)
          for (i in 0..instance.amplifier)
            costScale *= (instance.effect.value() as EmpoweredStatusEffect).scale
      cost = ((cost.toFloat()) * costScale).roundToInt()

      if (ScriptorConfig.VOCAL_MAX_COST() in 0..<cost)
        player.sendSystemMessage(Component.translatable("extra.scriptor.mute"))

      val adjustedCost = (cost * (ScriptorConfig.VOCAL_COOLDOWN_MULTIPLIER() / 100.0)).roundToInt()
      if (!player.isCreative) {
        player.addEffect(MobEffectInstance(HOARSE.ref(), adjustedCost))
        if (adjustedCost > ScriptorConfig.VOCAL_HUNGER_THRESHOLD())
          player.addEffect(
            MobEffectInstance(
              MobEffects.HUNGER,
              2 * (adjustedCost - ScriptorConfig.VOCAL_HUNGER_THRESHOLD())
            )
          )
        if (adjustedCost > ScriptorConfig.VOCAL_DAMAGE_THRESHOLD())
          player.hurt(overload(player)!!, (adjustedCost - ScriptorConfig.VOCAL_DAMAGE_THRESHOLD() * 0.75f) / 100f)
      }
      if (player.health > 0) spell.cast(EntityTargetable(player))
      if (!ScriptorConfig.SHOW_SPELLS_IN_CHAT()) return true
    }
    return false
  }

}