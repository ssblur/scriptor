package com.ssblur.scriptor.screen.screen

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.screen.menu.ScriptorMenus
import com.ssblur.unfocused.event.client.ClientScreenRegistrationEvent.registerScreen
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component

object ScriptorScreens {
  fun register() {
    ScriptorMenus.WRITING_TABLE.then{
      ScriptorMod.registerScreen(it) { container, inventory, component ->
        WritingTableScreen(container, inventory ?: Minecraft.getInstance().player!!.inventory, component ?: Component.empty())
      }
    }
  }
}