package com.ssblur.scriptor.events

import com.ssblur.scriptor.ScriptorDamage.overload
import com.ssblur.scriptor.config.ScriptorConfig
import com.ssblur.scriptor.data.saved_data.DictionarySavedData.Companion.computeIfAbsent
import com.ssblur.scriptor.effect.EmpoweredStatusEffect
import com.ssblur.scriptor.effect.ScriptorEffects.MUTE
import com.ssblur.scriptor.extension.EntityCastCooldownExtension.canCast
import com.ssblur.scriptor.extension.EntityCastCooldownExtension.castCooldown
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.unfocused.event.common.PlayerChatEvent
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import java.util.function.Predicate
import kotlin.math.roundToInt

object SpellChat {
  fun init() {
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

  fun castFromChat(entity: Entity, level: ServerLevel, sentence: String): Boolean {
    if (!ScriptorConfig.CHAT_CAST_ENABLED()) return false
    val player = entity as? Player
    val living = entity as? LivingEntity
    val spell = computeIfAbsent(level).parse(sentence)
    if (spell != null) {
      if (!entity.canCast(spell)) {
        player?.displayClientMessage(Component.translatable("extra.scriptor.hoarse"), true)
        return true
      } else if (living?.hasEffect(MUTE.ref()) == true) {
        player?.displayClientMessage(Component.translatable("extra.scriptor.mute"), true)
        return true
      }

      var cost = (spell.cost() * 30).roundToInt()
      var costScale = 1.0f
      for (instance in living?.activeEffects ?: listOf())
        if (instance.effect.value() is EmpoweredStatusEffect)
          (0..instance.amplifier).forEach { _ ->
            costScale *= (instance.effect.value() as EmpoweredStatusEffect).scale
          }
      cost = ((cost.toFloat()) * costScale).roundToInt()

      if (ScriptorConfig.VOCAL_MAX_COST() in 0..<cost)
        player?.displayClientMessage(Component.translatable("extra.scriptor.mute"), true)

      val adjustedCost = (cost * (ScriptorConfig.VOCAL_COOLDOWN_MULTIPLIER() / 100.0)).roundToInt()
      if (player?.isCreative != true) {
        entity.castCooldown = adjustedCost * 10L
        if (adjustedCost > ScriptorConfig.VOCAL_HUNGER_THRESHOLD())
          living?.addEffect(
            MobEffectInstance(
              MobEffects.HUNGER,
              2 * (adjustedCost - ScriptorConfig.VOCAL_HUNGER_THRESHOLD())
            )
          )
        if (adjustedCost > ScriptorConfig.VOCAL_DAMAGE_THRESHOLD())
          living?.hurt(overload(living)!!, (adjustedCost - ScriptorConfig.VOCAL_DAMAGE_THRESHOLD() * 0.75f) / 100f)
      }
      if ((living?.health ?: 0f) > 0) spell.cast(EntityTargetable(entity))
      if (!ScriptorConfig.SHOW_SPELLS_IN_CHAT()) return true
    }
    return false
  }

}