package com.ssblur.scriptor.helpers

import com.ssblur.scriptor.config.ScriptorConfig
import com.ssblur.scriptor.data.saved_data.DictionarySavedData
import com.ssblur.scriptor.registry.words.*
import com.ssblur.scriptor.word.PartialSpell
import com.ssblur.scriptor.word.Spell
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.phys.AABB

object FakeCastHelper {
  // TODO add evoker fangs spell
  // TODO add evoker summon spell
  val EVOKER_WOLOLO: Spell by lazy {
    Spell(Subjects.TOUCH, PartialSpell(Actions.COLOR, ColorDescriptors.RED))
  }
  val EVOKER_FANGS: Spell by lazy {
    Spell(Subjects.HITSCAN, PartialSpell(Actions.EVOKER_FANGS, PowerDescriptors.STRONG))
  }
  val EVOKER_SUMMON: Spell by lazy {
    Spell(Subjects.SELF, PartialSpell(null))
  }

  val DISAPPEAR: Spell by lazy {
    Spell(
      Subjects.SELF,
      PartialSpell(PotionActions.INVISIBILITY_POTION, Descriptors.LONG),
        PartialSpell(null)
    )
  }

  val ILLUSIONER_BLINDNESS: Spell by lazy {
    Spell(Subjects.PROJECTILE, PartialSpell(PotionActions.BLINDNESS_POTION))
  }

  val NOMINOMIST_HEAL: Spell by lazy {
    Spell(Subjects.TOUCH, PartialSpell(Actions.HEAL))
  }

  fun castAs(entity: LivingEntity, spell: Spell) {
    val level = entity.level()
    if(level is ServerLevel) {
      val sentence = DictionarySavedData.computeIfAbsent(level).generate(spell)
      val enabled = ScriptorConfig.ENTITY_CASTING()
      if(!enabled) return
      val range = ScriptorConfig.ENTITY_CASTING_PROXIMITY().toDouble()
      level.getNearbyPlayers(
        TargetingConditions.forNonCombat(),
        entity,
        AABB.ofSize(entity.position(), range, range, range)
      ).forEach {
        it.sendSystemMessage(
          Component.literal("<")
            .append(entity.name.copy().withStyle(ChatFormatting.LIGHT_PURPLE))
            .append("> ")
            .append(sentence)
        )
        if(spell.subject != null
          && spell.spells.all { s -> s.action != null && s.descriptors.count { d -> d == null } == 0 }) {
          ScriptionaryHelper.recordSpell(it, spell)
        }
      }
    }
  }

  fun LivingEntity.fakeCast(spell: Spell) = castAs(this, spell)
}