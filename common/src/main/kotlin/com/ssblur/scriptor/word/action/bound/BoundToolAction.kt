package com.ssblur.scriptor.word.action.bound

import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.color.CustomColors.getColor
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.helpers.ItemTargetableHelper
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.descriptor.duration.DurationDescriptor
import com.ssblur.scriptor.word.descriptor.power.StrengthDescriptor
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.DyedItemColor
import net.minecraft.world.item.component.Tool
import net.minecraft.world.level.block.Block
import java.util.function.Supplier
import kotlin.math.floor

class BoundToolAction(var item: Supplier<Item>, var tags: List<TagKey<Block>>) : Action() {
    override fun apply(caster: Targetable, targetable: Targetable, descriptors: Array<Descriptor>) {
        var strength = 6f
        var duration = 4.0
        for (d in descriptors) {
            if (d is StrengthDescriptor) strength += d.strengthModifier().toFloat()
            if (d is DurationDescriptor) duration += d.durationModifier()
        }

        val itemStack = ItemStack(item.get())

        itemStack.set(DataComponents.DYED_COLOR, DyedItemColor(getColor(descriptors), false))
        itemStack.set(
            ScriptorDataComponents.EXPIRES, caster.level.gameTime + floor(duration * 80)
                .toLong()
        )
        val finalStrength = (strength * 0.666f).toInt()
        val finalToolLevel = finalStrength / 2
        itemStack.set(ScriptorDataComponents.TOOL_MINING_LEVEL, finalToolLevel)
        itemStack.update(
            DataComponents.TOOL,
            Tool(listOf<Tool.Rule>(), 1f, 1)
        ) { tool: Tool ->
            val rules: MutableList<Tool.Rule> = ArrayList()
            if (finalToolLevel < 1) rules.add(Tool.Rule.deniesDrops(NEEDS_STONE_TOOL))
            else rules.add(Tool.Rule.minesAndDrops(NEEDS_STONE_TOOL, finalStrength.toFloat()))

            if (finalToolLevel < 2) rules.add(Tool.Rule.deniesDrops(NEEDS_IRON_TOOL))
            else rules.add(Tool.Rule.minesAndDrops(NEEDS_IRON_TOOL, finalStrength.toFloat()))

            if (finalToolLevel < 3) rules.add(Tool.Rule.deniesDrops(NEEDS_DIAMOND_TOOL))
            else rules.add(Tool.Rule.minesAndDrops(NEEDS_DIAMOND_TOOL, finalStrength.toFloat()))

            if (finalToolLevel < 4) rules.add(Tool.Rule.deniesDrops(NEEDS_NETHERITE_TOOL))
            else rules.add(Tool.Rule.minesAndDrops(NEEDS_NETHERITE_TOOL, finalStrength.toFloat()))

            rules.addAll(
                tags.stream().map { Tool.Rule.minesAndDrops(it, finalStrength.toFloat()) }
                    .toList()
            )
            Tool(rules, 1f, tool.damagePerBlock())
        }

        ItemTargetableHelper.depositItemStack(targetable, itemStack)
    }

    override fun cost(): Cost {
        return Cost(6.0, COSTTYPE.ADDITIVE)
    }

    companion object {
        val NEEDS_STONE_TOOL: TagKey<Block> =
            TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("needs_stone_tool"))
        val NEEDS_IRON_TOOL: TagKey<Block> =
            TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("needs_iron_tool"))
        val NEEDS_DIAMOND_TOOL: TagKey<Block> =
            TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("needs_diamond_tool"))
        val NEEDS_NETHERITE_TOOL: TagKey<Block> =
            TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("needs_netherite_tool"))
    }
}
