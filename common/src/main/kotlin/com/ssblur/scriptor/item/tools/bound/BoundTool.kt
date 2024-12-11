package com.ssblur.scriptor.item.tools.bound

import com.ssblur.scriptor.color.CustomColors.getColor
import dev.architectury.platform.Platform
import dev.architectury.registry.client.rendering.ColorHandlerRegistry
import net.fabricmc.api.EnvType
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.level.block.Block

class BoundTool(tier: Tier, tagKey: TagKey<Block>, properties: Properties) :
    DiggerItem(tier, tagKey, properties) {
    init {
        if (Platform.getEnv() == EnvType.CLIENT) ColorHandlerRegistry.registerItemColors(
            { itemStack: ItemStack?, t: Int ->
                if (t == 1) getColor(
                    itemStack!!
                ) else -0x1
            }, this
        )
    }
}
