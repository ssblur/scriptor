package com.ssblur.scriptor.helpers

import com.ssblur.scriptor.ScriptorMod.COMMUNITY_MODE
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import net.minecraft.ChatFormatting
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.world.item.ItemStack

@Suppress("unused")
object ComponentHelper {
    const val DEFAULT_WIDTH: Int = 38

    fun updateTooltipWith(size: Int, list: MutableList<Component>, key: String, vararg substitutions: Any?) {
        updateTooltipWith(size, Style.EMPTY, list, key, *substitutions)
    }

    fun updateTooltipWith(list: MutableList<Component>, key: String, vararg substitutions: Any?) {
        updateTooltipWith(DEFAULT_WIDTH, list, key, *substitutions)
    }

    fun updateTooltipWith(
        style: ChatFormatting,
        list: MutableList<Component>,
        key: String,
        vararg substitutions: Any?
    ) {
        updateTooltipWith(DEFAULT_WIDTH, style, list, key, *substitutions)
    }

    fun updateTooltipWith(style: Style, list: MutableList<Component>, key: String, vararg substitutions: Any?) {
        updateTooltipWith(DEFAULT_WIDTH, style, list, key, *substitutions)
    }

    fun updateTooltipWith(
        size: Int,
        style: Style,
        list: MutableList<Component>,
        key: String,
        vararg substitutions: Any?
    ) {
        list.addAll(
            orderedTranslatableComponents(size, key, *substitutions).stream()
                .map { component: MutableComponent -> component.withStyle(style) }
                .toList())
    }

    fun updateTooltipWith(
        size: Int,
        style: ChatFormatting,
        list: MutableList<Component>,
        key: String,
        vararg substitutions: Any?
    ) {
        list.addAll(
            orderedTranslatableComponents(size, key, *substitutions).stream()
                .map { component: MutableComponent -> component.withStyle(style) }
                .toList())
    }

    fun orderedTranslatableComponents(size: Int, key: String, vararg substitutions: Any?): List<MutableComponent> {
        val text = I18n.get(key, *substitutions)
        val tokens = text.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val list = ArrayList<String>()
        var line = StringBuilder()
        for (token in tokens) {
            if (token.length >= size) {
                list.add(line.toString())
                list.add(token)
                line = StringBuilder()
            } else if ((token.length + line.length) >= size) {
                list.add(line.toString())
                line = StringBuilder()
                line.append(token)
                line.append(" ")
            } else {
                line.append(token)
                line.append(" ")
            }
        }
        list.add(line.toString())

        return list.stream().map(Component::literal).toList()
    }

    fun addCommunityDisclaimer(list: MutableList<Component>, itemStack: ItemStack) {
        if (itemStack.getOrDefault(ScriptorDataComponents.COMMUNITY_MODE, false) && !COMMUNITY_MODE) {
            list.add(Component.translatable("lore.scriptor.not_community_2").withStyle(ChatFormatting.RED))
            list.add(Component.translatable("lore.scriptor.not_community_3").withStyle(ChatFormatting.RED))
        } else if (COMMUNITY_MODE) {
            list.add(Component.translatable("lore.scriptor.not_community_1").withStyle(ChatFormatting.RED))
            list.add(Component.translatable("lore.scriptor.not_community_3").withStyle(ChatFormatting.RED))
        }
    }
}
