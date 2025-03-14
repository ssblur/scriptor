package com.ssblur.scriptor.blockentity

import com.ssblur.scriptor.block.WritingTableBlock
import com.ssblur.scriptor.item.ScriptorItems
import com.ssblur.scriptor.item.ScriptorTags
import com.ssblur.scriptor.screen.menu.WritingTableMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.ContainerHelper
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.player.StackedContents
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.StackedContentsCompatible
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class WritingTableBlockEntity(blockPos: BlockPos, blockState: BlockState):
  BaseContainerBlockEntity(ScriptorBlockEntities.WRITING_TABLE.get(), blockPos, blockState), WorldlyContainer, StackedContentsCompatible {
  var tableItems: NonNullList<ItemStack>

  init {
    tableItems = NonNullList.withSize(2, ItemStack.EMPTY)
  }

  var dictionary: ItemStack
    get() = tableItems[DICTIONARY_SLOT]
    set(itemStack) {
      tableItems[DICTIONARY_SLOT] = itemStack
      if (level != null) level!!.sendBlockUpdated(blockPos, blockState, blockState, 3)
      setChanged()
    }

  var book: ItemStack
    get() = tableItems[BOOK_SLOT]
    set(itemStack) {
      tableItems[BOOK_SLOT] = itemStack
      if (level != null) level!!.sendBlockUpdated(blockPos, blockState, blockState, 3)
      setChanged()
    }

  override fun getUpdatePacket(): Packet<ClientGamePacketListener>? {
    return ClientboundBlockEntityDataPacket.create(this)
  }

  override fun getUpdateTag(provider: HolderLookup.Provider): CompoundTag {
    val tag = super.getUpdateTag(provider)
    saveAdditional(tag, provider)
    return tag
  }

  public override fun loadAdditional(tag: CompoundTag, provider: HolderLookup.Provider) {
    super.loadAdditional(tag, provider)
    tableItems = NonNullList.withSize(2, ItemStack.EMPTY)
    ContainerHelper.loadAllItems(tag, tableItems, provider)
    setChanged()
  }

  override fun saveAdditional(tag: CompoundTag, provider: HolderLookup.Provider) {
    super.saveAdditional(tag, provider)
    ContainerHelper.saveAllItems(tag, tableItems, provider)
  }

  fun tick() {
    if (level == null || level!!.isClientSide) return
    if(blockState.getValue(WritingTableBlock.BOOK) == dictionary.isEmpty)
      level?.setBlockAndUpdate(blockPos, blockState.setValue(WritingTableBlock.BOOK, !dictionary.isEmpty))
  }


  companion object {
    const val DICTIONARY_SLOT: Int = 0
    const val BOOK_SLOT: Int = 1

    fun tick(level: Level, blockEntity: BlockEntity) {
      if (level.isClientSide) return
      if (blockEntity is WritingTableBlockEntity) blockEntity.tick()
    }
  }

  override fun clearContent() {
    tableItems.clear()
  }

  override fun getContainerSize() = 2

  override fun isEmpty() = tableItems.isEmpty()
  override fun getItem(i: Int) = tableItems.get(i)
  override fun removeItem(i: Int, j: Int): ItemStack {
    setChanged()
    val out = tableItems[i].copyWithCount(j)
    tableItems[i].shrink(j)
    return out
  }

  override fun removeItemNoUpdate(i: Int): ItemStack {
    val stack: ItemStack = tableItems[i]
    tableItems[i] = ItemStack.EMPTY
    return stack
  }

  override fun setItem(i: Int, itemStack: ItemStack) {
    setChanged()
    tableItems[i] = itemStack
  }

  override fun stillValid(player: Player) =
    player.distanceToSqr(blockPos.center) < player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE)

  override fun createMenu(i: Int, inventory: Inventory): AbstractContainerMenu = WritingTableMenu(i, inventory, this)
  override fun getDefaultName(): Component = Component.translatable("menu.scriptor.writing_table")
  override fun getItems(): NonNullList<ItemStack> = tableItems
  override fun setItems(nonNullList: NonNullList<ItemStack>) {
    tableItems = nonNullList
  }

  override fun getSlotsForFace(direction: Direction): IntArray {
    return when(direction) {
      Direction.DOWN -> intArrayOf(BOOK_SLOT)
      Direction.UP -> intArrayOf(DICTIONARY_SLOT)
      else -> intArrayOf()
    }
  }

  override fun canPlaceItemThroughFace(i: Int, itemStack: ItemStack, direction: Direction?): Boolean {
    if(direction == null || i in getSlotsForFace(direction)) {
      if(i == BOOK_SLOT && itemStack.`is`(ScriptorTags.WRITABLE_SPELLBOOKS)) return true
      if(i == DICTIONARY_SLOT && itemStack.`is`(ScriptorItems.DICTIONARY)) return true
    }
    return false
  }

  override fun canTakeItemThroughFace(i: Int, itemStack: ItemStack, direction: Direction): Boolean {
    if(i in getSlotsForFace(direction)) {
      if(i == BOOK_SLOT && itemStack.`is`(ScriptorTags.WRITABLE_SPELLBOOKS)) return true
      if(i == DICTIONARY_SLOT && itemStack.`is`(ScriptorItems.DICTIONARY)) return true
    }
    return false
  }

  override fun fillStackedContents(stackedContents: StackedContents) {
    for (itemStack in tableItems) stackedContents.accountStack(itemStack)
  }
}
