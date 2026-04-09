package com.ssblur.scriptor.villagers

import com.google.common.collect.ImmutableSet
import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.block.ScriptorBlocks
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.ai.village.poi.PoiType
import net.minecraft.world.entity.npc.VillagerProfession

object ScriptorVillagers {
  val NOMINOMIST_POI = ScriptorMod.registerPointOfInterest("nominomist_poi") {
    PoiType(ScriptorBlocks.WRITING_TABLE.first.get().stateDefinition.possibleStates.toSet(), 1, 3)
  }

  val NOMINOMIST = ScriptorMod.registerProfession("nominomist") {
    VillagerProfession(
      "nominomist",
      { it.value() == NOMINOMIST_POI.get() },
      { it.value() == NOMINOMIST_POI.get() },
      ImmutableSet.of(),
      ImmutableSet.of(ScriptorBlocks.CHALK.get()),
      SoundEvents.EVOKER_CAST_SPELL
    )
  }

  fun register() {}
}