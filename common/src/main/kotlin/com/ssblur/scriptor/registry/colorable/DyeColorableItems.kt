package com.ssblur.scriptor.registry.colorable

import com.ssblur.scriptor.color.DyeColorableItem
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Items

object DyeColorableItems {
  val DYE = DyeColorableItem()
  
  init {
    DYE.add(Items.BLACK_DYE, DyeColor.BLACK)
    DYE.add(Items.WHITE_DYE, DyeColor.WHITE)
    DYE.add(Items.BLUE_DYE, DyeColor.BLUE)
    DYE.add(Items.BROWN_DYE, DyeColor.BROWN)
    DYE.add(Items.CYAN_DYE, DyeColor.CYAN)
    DYE.add(Items.GRAY_DYE, DyeColor.GRAY)
    DYE.add(Items.GREEN_DYE, DyeColor.GREEN)
    DYE.add(Items.LIGHT_BLUE_DYE, DyeColor.LIGHT_BLUE)
    DYE.add(Items.LIGHT_GRAY_DYE, DyeColor.LIGHT_GRAY)
    DYE.add(Items.LIME_DYE, DyeColor.LIME)
    DYE.add(Items.MAGENTA_DYE, DyeColor.MAGENTA)
    DYE.add(Items.ORANGE_DYE, DyeColor.ORANGE)
    DYE.add(Items.PINK_DYE, DyeColor.PINK)
    DYE.add(Items.PURPLE_DYE, DyeColor.PURPLE)
    DYE.add(Items.RED_DYE, DyeColor.RED)
    DYE.add(Items.YELLOW_DYE, DyeColor.YELLOW)
    DYE.register()
  }
}