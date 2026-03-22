package com.ssblur.scriptor.events

import com.ssblur.scriptor.ScriptorMod.COMMUNITY_MODE
import com.ssblur.scriptor.advancement.ScriptorAdvancements.COMMUNITY
import com.ssblur.scriptor.config.ScriptorConfig
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.data.saved_data.DictionarySavedData.Companion.computeIfAbsent
import com.ssblur.scriptor.data.saved_data.PlayerSpellsSavedData.Companion.computeIfAbsent
import com.ssblur.scriptor.extension.EntityCastCooldownExtension.castCooldown
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.network.client.ScriptorNetworkS2C
import com.ssblur.scriptor.network.client.ScriptorNetworkS2C.color
import com.ssblur.scriptor.network.client.ScriptorNetworkS2C.flag
import com.ssblur.scriptor.registry.words.Subjects
import com.ssblur.scriptor.resources.Colors.cache
import com.ssblur.scriptor.resources.MobSpellItems
import com.ssblur.unfocused.event.common.EntityDamagedEvent
import com.ssblur.unfocused.event.common.MobSpawnEvent
import com.ssblur.unfocused.event.common.PlayerJoinedEvent
import com.ssblur.unfocused.event.common.ServerStartEvent
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.Mob
import kotlin.math.roundToLong

object ScriptorEvents {
  var invertDoNotPhaseMemory = false
  fun register() {
    ServerStartEvent.register {
      computeIfAbsent(it.overworld())
      it.addTickable {
        if(ScriptorConfig.INVERT_DO_NOT_PHASE() != invertDoNotPhaseMemory) {
          invertDoNotPhaseMemory = ScriptorConfig.INVERT_DO_NOT_PHASE()
          flag.invoke(ScriptorNetworkS2C.Flag(ScriptorNetworkS2C.FLAGS.INVERT_DO_NOT_PHASE, ScriptorConfig.INVERT_DO_NOT_PHASE()), it.playerList.players)
        }
      }
    }

    PlayerJoinedEvent.register { player ->
      if (COMMUNITY_MODE) COMMUNITY.get().trigger(player)
      flag.invoke(ScriptorNetworkS2C.Flag(ScriptorNetworkS2C.FLAGS.COMMUNITY, COMMUNITY_MODE), listOf(player))
      flag.invoke(ScriptorNetworkS2C.Flag(ScriptorNetworkS2C.FLAGS.INVERT_DO_NOT_PHASE, ScriptorConfig.INVERT_DO_NOT_PHASE()), listOf(player))
      computeIfAbsent(player)
      for (item in cache)
        color(ScriptorNetworkS2C.Color(item.b!!, item.a!!, item.c[0], item.c[1], item.c[2]), listOf(player))
    }


    EntityDamagedEvent.Before.register { (entity, source, _) ->
      source.weaponItem?.let { weapon ->
        weapon[ScriptorDataComponents.CHARGES]?.let { charges ->
          if(charges > 0) {
            entity.health -= 5

            if(charges == 1) weapon.remove(ScriptorDataComponents.CHARGES)
            else weapon.set(ScriptorDataComponents.CHARGES, charges - 1)
          }
        }

        EquipmentSlot.entries.forEach { slot ->
          entity.getItemBySlot(slot)[ScriptorDataComponents.SPELL]?.let { spell ->
            entity.level().let {
              if(it is ServerLevel) {
                val parsed = computeIfAbsent(it).parse(spell)
                if(parsed?.subject == Subjects.ON_DAMAGED && entity.castCooldown <= 0) {
                  entity.castCooldown = (parsed.cost() * 20.0 * 4.0).roundToLong()
                  parsed.castOnTargets(
                    EntityTargetable(entity),
                    listOf(
                      if(source.entity != null)
                        EntityTargetable(source.entity!!)
                      else
                        Targetable(it, source.sourcePosition ?: entity.position())
                    )
                  )
                }
              }
            }

          }
        }

        weapon[ScriptorDataComponents.SPELL]?.let { spell ->
          entity.level().let {
            if(it is ServerLevel) {
              val parsed = computeIfAbsent(it).parse(spell)
              if(parsed?.subject == Subjects.ON_HIT && (source.entity?.castCooldown ?: 1) <= 0) {
                source.entity!!.castCooldown = (parsed.cost() * 20.0 * 4.0).roundToLong()
                parsed.castOnTargets(
                    EntityTargetable(source.entity!!),
                  listOf(EntityTargetable(entity))
                )
              }
            }
          }
        }
      }
    }

    MobSpawnEvent.register { (entity, level) ->
      if(entity is Mob && level is ServerLevel) MobSpellItems.giveItem(entity)
    }

    SpellChat.init()
    PlayerTick.init()
    AddLootEvent.init()
  }
}