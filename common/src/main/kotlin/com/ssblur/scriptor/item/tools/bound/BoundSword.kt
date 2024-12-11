package com.ssblur.scriptor.item.tools.bound

import com.ssblur.scriptor.color.CustomColors.getColor
import dev.architectury.platform.Platform
import dev.architectury.registry.client.rendering.ColorHandlerRegistry
import net.fabricmc.api.EnvType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.Tier

class BoundSword(tier: Tier, properties: Properties) : SwordItem(tier, properties) {
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
