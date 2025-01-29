package com.ssblur.scriptor.resources

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.ScriptorMod.LOGGER
import com.ssblur.scriptor.registry.words.WordRegistry.register
import com.ssblur.scriptor.word.descriptor.discount.ReagentDescriptor
import com.ssblur.unfocused.data.DataLoaderRegistry.registerDataLoader
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation

object Reagents {
  data class Reagent(val item: String, val cost: Int)

  init {
    ScriptorMod.registerDataLoader("scriptor/reagents", Reagent::class) { reagent, location ->
      LOGGER.info("Loaded reagent {}: {} / {}", location, reagent.item, reagent.cost)
      register(
        "reagent." + location.toShortLanguageKey(),
        ReagentDescriptor(BuiltInRegistries.ITEM[ResourceLocation.parse(reagent.item)], reagent.cost)
      )
    }
  }
}