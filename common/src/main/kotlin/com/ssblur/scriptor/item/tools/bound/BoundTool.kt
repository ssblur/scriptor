package com.ssblur.scriptor.item.tools.bound

import com.ssblur.scriptor.color.CustomColors.getColor
import com.ssblur.unfocused.helper.ColorHelper.registerColor
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.level.block.Block

class BoundTool(tier: Tier, tagKey: TagKey<Block>, properties: Properties):
  DiggerItem(tier, tagKey, properties) {
  init {
    try {
      clientInit()
    } catch (_: NoSuchMethodError) {
    }
  }

  @Environment(EnvType.CLIENT)
  fun clientInit() {
    this.registerColor { itemStack: ItemStack?, t: Int ->
      if (t == 1) getColor(
        itemStack!!
      ) else -0x1
    }
  }
}
