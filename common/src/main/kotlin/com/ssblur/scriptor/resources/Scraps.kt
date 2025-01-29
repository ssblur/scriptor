package com.ssblur.scriptor.resources

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.ScriptorMod.COMMUNITY_MODE
import com.ssblur.scriptor.advancement.ScriptorAdvancements.TOME
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.data.saved_data.DictionarySavedData.Companion.computeIfAbsent
import com.ssblur.scriptor.data.saved_data.PlayerSpellsSavedData.Companion.computeIfAbsent
import com.ssblur.scriptor.item.ScriptorItems
import com.ssblur.unfocused.data.DataLoaderRegistry.registerSimpleDataLoader
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import kotlin.math.min
import kotlin.random.Random

object Scraps {
  data class ScrapResource(var keys: List<String>, var tier: Int = 0)

  val scraps = ScriptorMod.registerSimpleDataLoader("scriptor/scraps", ScrapResource::class)
  val random = Random(System.nanoTime())
  fun tier(tier: Int) = scraps.entries.filter { it.value.tier == tier }.toTypedArray()

  fun getRandomScrap(t: Int, player: Player): String {
    var options = tier(t)
    if (COMMUNITY_MODE) {
      val level = player.level()
      if (level is ServerLevel) {
        var bracket: Int = level.seed.toInt() % 5
        bracket = min((bracket + 10) % 5, (options.size - 1))

        val filtered: MutableList<MutableMap.MutableEntry<ResourceLocation, ScrapResource>> = mutableListOf()
        for (i in options.indices) if (bracket == i % 5) filtered.add(options[i])
        options = filtered.toTypedArray()
      }
    }

    val data = computeIfAbsent(player)
    if (data != null) {
      val known = data.getScrapTier(t)
      TOME.get().trigger(player as ServerPlayer)
      if (options.size <= known.size) return options[random.nextInt(options.size)].value.keys.random()

      var maxAttempts = 10
      var resource: MutableMap.MutableEntry<ResourceLocation, ScrapResource>
      var index: Int
      var key: ResourceLocation
      do {
        resource = options.random()
        index = resource.value.keys.indices.random()
        key = resource.key.withSuffix("." + resource.value.keys[index].replace(":", "."))

        maxAttempts--
      } while (maxAttempts > 0 && known.containsKey(key.toShortLanguageKey()))
      known[key.toShortLanguageKey()] = true
      data.setDirty()
      return resource.value.keys[index]
    }
    return options[Tomes.random.nextInt(options.size)].value.keys.random()
  }

  fun getRandomScrapItem(t: Int, player: Player): ItemStack {
    val scrap = getRandomScrap(t, player)
    val itemStack = ItemStack(ScriptorItems.SCRAP)

    itemStack[ScriptorDataComponents.SPELL] = scrap
    val word = computeIfAbsent((player.level() as ServerLevel)).getWord(scrap)
    if (word != null)
      itemStack[DataComponents.ITEM_NAME] = Component.literal(word)
    else
      itemStack[DataComponents.ITEM_NAME] = Component.literal("Invalid word: $scrap")
    if (COMMUNITY_MODE) itemStack.set(ScriptorDataComponents.COMMUNITY_MODE, true)

    return itemStack
  }
}