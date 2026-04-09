package com.ssblur.scriptor.screen.menu

import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class DictionaryMenu(i: Int, val inventory: Inventory? = null) : AbstractContainerMenu(ScriptorMenus.DICTIONARY_MENU.get(), i) {
  override fun quickMoveStack(player: Player, i: Int): ItemStack = ItemStack.EMPTY
  override fun stillValid(player: Player) = true

  private val container = SimpleContainer(1)
  private val slot: Slot = addSlot(Slot(container, 0, Int.MAX_VALUE, Int.MAX_VALUE))

  var dictionary: ItemStack
    get() = slot.item
    set(value) = slot.set(value)
}