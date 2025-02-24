package com.ssblur.scriptor.screen.menu

import com.ssblur.scriptor.ScriptorMod
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.inventory.MenuType

object ScriptorMenus {
  val WRITING_TABLE = ScriptorMod.registerMenu("writing_table") { MenuType(::WritingTableMenu, FeatureFlagSet.of()) }

  fun register() {}
}