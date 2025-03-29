package com.ssblur.scriptor.screen.menu

import com.mojang.datafixers.util.Pair
import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.blockentity.WritingTableBlockEntity
import com.ssblur.scriptor.item.ScriptorItems
import com.ssblur.scriptor.item.ScriptorTags
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.*
import net.minecraft.world.item.ItemStack

class WritingTableMenu(i: Int, val inventory: Inventory, val table: WritingTableBlockEntity? = null): AbstractContainerMenu(ScriptorMenus.WRITING_TABLE.get(), i) {
  val containerData: ContainerData
  val dictionary: ItemStack
    get() = getSlot(DICTIONARY_SLOT).item
  val book: ItemStack
    get() = getSlot(BOOK_SLOT).item
  val pos: BlockPos
    get() {
      if(table != null) return table.blockPos
      return BlockPos(containerData.get(X_SLOT), containerData.get(Y_SLOT), containerData.get(Z_SLOT))
    }

  init {
    val container = table ?: SimpleContainer(2)
    this.addSlot(object: Slot(container, DICTIONARY_SLOT, 180, 105){
      override fun getNoItemIcon(): Pair<ResourceLocation, ResourceLocation> {
        return Pair(InventoryMenu.BLOCK_ATLAS, ScriptorMod.location("item/empty_dictionary_slot"))
      }
    })
    this.addSlot(object: Slot(container, BOOK_SLOT, 180, 129){
      override fun getNoItemIcon(): Pair<ResourceLocation, ResourceLocation> {
        return Pair(InventoryMenu.BLOCK_ATLAS, ScriptorMod.location("item/empty_book_slot"))
      }
    })

    for (j in 0..2) {
      for (k in 0..8) {
        this.addSlot(Slot(inventory, k + j * 9 + 9, 8 + k * 18, 100 + j * 18))
      }
    }

    for (j in 0..8) {
      this.addSlot(Slot(inventory, j, 8 + j * 18, 158))
    }

    containerData = SimpleContainerData(5)
    table?.let { table ->
      containerData.set(X_SLOT, table.blockPos.x)
      containerData.set(Y_SLOT, table.blockPos.y)
      containerData.set(Z_SLOT, table.blockPos.z)
    }
    addDataSlots(containerData)
  }

  companion object {
    const val DICTIONARY_SLOT = WritingTableBlockEntity.DICTIONARY_SLOT
    const val BOOK_SLOT = WritingTableBlockEntity.BOOK_SLOT
    private const val X_SLOT = 2
    private const val Y_SLOT = 3
    private const val Z_SLOT = 4
  }

  override fun quickMoveStack(player: Player, i: Int): ItemStack {
    val itemStack = ItemStack.EMPTY
    val slot = slots[i]
    if (slot.hasItem()) {
      println(i)
      val item = slot.item
      if(i > 1) {
        if(item.`is`(ScriptorTags.WRITABLE_SPELLBOOKS) || item.`is`(ScriptorTags.READABLE_SPELLBOOKS)) {
          if(moveItemStackTo(item, BOOK_SLOT, BOOK_SLOT + 1, false))
            return item.copy()
          return ItemStack.EMPTY
        } else if(item.`is`(ScriptorItems.DICTIONARY.get())) {
          if(moveItemStackTo(item, DICTIONARY_SLOT, DICTIONARY_SLOT + 1, false))
            return item.copy()
          return ItemStack.EMPTY
        }
      } else {
        if(moveItemStackTo(item, 2, 38, true))
          return item.copy()
        return ItemStack.EMPTY
      }
    }

    return itemStack
  }

  override fun moveItemStackTo(itemStack: ItemStack, i: Int, j: Int, bl: Boolean): Boolean {
    return super.moveItemStackTo(itemStack, i, j, bl)
  }

  override fun stillValid(player: Player): Boolean = true
}