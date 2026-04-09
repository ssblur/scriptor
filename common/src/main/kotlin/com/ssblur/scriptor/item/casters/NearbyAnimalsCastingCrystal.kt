package com.ssblur.scriptor.item.casters

import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB

class NearbyAnimalsCastingCrystal(properties: Properties): CasterCrystal(properties) {
  override fun getTargetables(itemStack: ItemStack?, level: Level?, caster: Targetable): List<Targetable?> {
    return level
      ?.getEntitiesOfClass(Animal::class.java, AABB.ofSize(caster.targetPos, 8.0, 8.0, 8.0))
      ?.map { EntityTargetable(it) }
      ?: listOf()
  }

  override fun appendHoverText(
    itemStack: ItemStack,
    level: TooltipContext,
    list: MutableList<Component>,
    tooltipFlag: TooltipFlag
  ) {
    super.appendHoverText(itemStack, level, list, tooltipFlag)
    list.add(Component.translatable("lore.scriptor.animal_crystal").withStyle(ChatFormatting.GRAY))
    list.add(Component.translatable("lore.scriptor.crystal_focus").withStyle(ChatFormatting.GRAY))
  }
}
