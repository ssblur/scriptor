package com.ssblur.scriptor.events

import com.ssblur.scriptor.ScriptorMod.COMMUNITY_MODE
import com.ssblur.scriptor.advancement.ScriptorAdvancements.COMMUNITY
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.data.saved_data.DictionarySavedData.Companion.computeIfAbsent
import com.ssblur.scriptor.data.saved_data.PlayerSpellsSavedData.Companion.computeIfAbsent
import com.ssblur.scriptor.network.client.ScriptorNetworkS2C
import com.ssblur.scriptor.network.client.ScriptorNetworkS2C.color
import com.ssblur.scriptor.network.client.ScriptorNetworkS2C.flag
import com.ssblur.scriptor.resources.Colors.cache
import com.ssblur.unfocused.event.common.EntityDamagedEvent
import com.ssblur.unfocused.event.common.PlayerJoinedEvent
import com.ssblur.unfocused.event.common.ServerStartEvent
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

object ScriptorEvents {
    fun register() {
        ServerStartEvent.register{ computeIfAbsent(it.overworld()) }
        PlayerJoinedEvent.register{ player ->
            if (COMMUNITY_MODE) COMMUNITY.get().trigger(player)
            flag.invoke(ScriptorNetworkS2C.Flag(ScriptorNetworkS2C.FLAGS.COMMUNITY, COMMUNITY_MODE), listOf(player))
            computeIfAbsent(player)
            for (item in cache)
                color(ScriptorNetworkS2C.Color(item.b!!, item.a!!, item.c[0], item.c[1], item.c[2]), listOf(player))
        }
        EntityDamagedEvent.Before.register{(entity, source, _) ->
            val weapon = source.weaponItem
            if(weapon != null) {
                val data = weapon[ScriptorDataComponents.CHARGES] ?: 0
                if (data > 0) {
                    entity.health -= 3
                    weapon.set(ScriptorDataComponents.CHARGES, data - 1)
                }
            }
        }
        SpellChat
        PlayerTick
        AddLootEvent

        try{ clientRegister() } catch (_: NoSuchMethodError) {}
    }

    @Environment(EnvType.CLIENT)
    fun clientRegister() {
        ScriptorClientEvents
    }
}