package com.ssblur.scriptor.resources

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.ScriptorMod.COMMUNITY_MODE
import com.ssblur.scriptor.advancement.ScriptorAdvancements.TOME
import com.ssblur.scriptor.advancement.ScriptorAdvancements.TOME_1
import com.ssblur.scriptor.advancement.ScriptorAdvancements.TOME_2
import com.ssblur.scriptor.advancement.ScriptorAdvancements.TOME_3
import com.ssblur.scriptor.advancement.ScriptorAdvancements.TOME_4
import com.ssblur.scriptor.data.saved_data.PlayerSpellsSavedData.Companion.computeIfAbsent
import com.ssblur.scriptor.helpers.resource.TomeResource
import com.ssblur.unfocused.data.DataLoaderRegistry.registerSimpleDataLoader
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import kotlin.math.min
import kotlin.random.Random

object Tomes {
    val tomes = ScriptorMod.registerSimpleDataLoader("scriptor/tomes", TomeResource::class)
    val random = Random(System.nanoTime())
    fun tier(tier: Int) = tomes.entries.filter { it.value.tier == tier }.toTypedArray()

    fun getRandomTome(t: Int, player: Player): TomeResource {
        var options = tier(t)
        if (COMMUNITY_MODE) {
            val level = player.level()
            if (level is ServerLevel) {
                var bracket: Int = level.seed.toInt() % 5
                bracket = min((bracket + 10) % 5, (options.size - 1))

                val filtered: MutableList<MutableMap.MutableEntry<ResourceLocation, TomeResource>> = mutableListOf()
                for (i in options.indices) if (bracket == i % 5) filtered.add(options[i])
                options = filtered.toTypedArray()
            }
        }

        val data = computeIfAbsent(player)
        if (data != null) {
            val known = data.getTier(t)
            TOME.get().trigger(player as ServerPlayer)
            if (options.size <= known.size) return options[random.nextInt(options.size)].value
            else if (options.size <= known.size + 1) {
                if (t == 1) TOME_1.get().trigger(player)
                if (t == 2) TOME_2.get().trigger(player)
                if (t == 3) TOME_3.get().trigger(player)
                if (t == 4) TOME_4.get().trigger(player)
            }

            var maxAttempts = 10
            var option: MutableMap.MutableEntry<ResourceLocation, TomeResource>
            do {
                option = options[random.nextInt(options.size)]
                maxAttempts--
            } while (maxAttempts > 0 && known.containsKey(option.key.toShortLanguageKey()))
            known[option.key.toShortLanguageKey()] = true
            data.setDirty()
            return option.value
        }
        return options[random.nextInt(options.size)].value
    }
}