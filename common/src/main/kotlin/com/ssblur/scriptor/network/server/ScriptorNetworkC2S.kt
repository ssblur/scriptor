package com.ssblur.scriptor.network.server

import com.ssblur.scriptor.ScriptorMod.location
import com.ssblur.scriptor.block.ScriptorBlocks
import com.ssblur.scriptor.blockentity.ChalkBlockEntity
import com.ssblur.scriptor.blockentity.EngravingBlockEntity
import com.ssblur.scriptor.config.ScriptorConfig
import com.ssblur.scriptor.data.components.BookOfBooksData
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.data.saved_data.DictionarySavedData.Companion.computeIfAbsent
import com.ssblur.scriptor.helpers.LimitedBookSerializer.decodeText
import com.ssblur.scriptor.helpers.SpellbookHelper
import com.ssblur.scriptor.helpers.targetable.ItemTargetable
import com.ssblur.scriptor.item.books.BookOfBooks
import com.ssblur.scriptor.item.tools.Chalk
import com.ssblur.scriptor.network.client.ScriptorNetworkS2C
import com.ssblur.unfocused.network.NetworkManager
import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.BlockHitResult
import kotlin.math.sign

@Suppress("unused")
object ScriptorNetworkC2S {
  data class SendChalk(val hitResult: BlockHitResult, val permanent: Boolean)

  val sendChalk =
    NetworkManager.registerC2S(location("server_receive_chalk_message"), SendChalk::class) { payload, player ->
      var itemStack = player.getItemInHand(InteractionHand.MAIN_HAND)
      if (itemStack.item !is Chalk) itemStack = player.getItemInHand(InteractionHand.OFF_HAND)
      if (itemStack.item !is Chalk) return@registerC2S

      val pos = payload.hitResult.blockPos.relative(payload.hitResult.direction)
      if (player.level().getBlockState(pos).canBeReplaced()) {
        val level = player.level()
        val blockEntity: ChalkBlockEntity
        if (payload.permanent) {
          level.setBlock(pos, ScriptorBlocks.ENGRAVING.get().defaultBlockState(), 11)
          blockEntity = EngravingBlockEntity(pos, level.getBlockState(pos))
        } else {
          level.setBlock(pos, ScriptorBlocks.CHALK.get().defaultBlockState(), 11)
          blockEntity = ChalkBlockEntity(pos, level.getBlockState(pos))
        }

        blockEntity.word = itemStack.hoverName.string
        blockEntity.facing = player.direction
        level.setBlockEntity(blockEntity)

        val blockState = level.getBlockState(pos)
        level.sendBlockUpdated(pos, blockState, blockState, 11)
      }
    }

  data class CreativeIdentify(val slot: Int, val spell: String)

  val creativeIdentify =
    NetworkManager.registerC2S(location("server_cursor_use_scrollc"), CreativeIdentify::class) { payload, player ->
      val level = player.level()
      if (level is ServerLevel) {
        val tokens = computeIfAbsent(level).parseComponents(payload.spell) ?: return@registerC2S
        ScriptorNetworkS2C.identify(ScriptorNetworkS2C.Identify(tokens.filterNotNull(), payload.slot), listOf(player))
      }
    }

  data class Identify(val slot: Int)

  val identify = NetworkManager.registerC2S(location("server_cursor_use_scroll"), Identify::class) { payload, player ->
    val level = player.level()
    val item = player.containerMenu.items[payload.slot]
    val carried = player.containerMenu.carried
    if (carried.isEmpty) return@registerC2S

    val text = item.get(DataComponents.WRITTEN_BOOK_CONTENT)
    if (text != null && level is ServerLevel) {
      val tokens =
        computeIfAbsent(level).parseComponents(decodeText(text))?.filterNotNull()?.toMutableList() ?: return@registerC2S
      var identified = item.get(ScriptorDataComponents.IDENTIFIED)
      identified = if (identified == null) ArrayList() else ArrayList(identified)
      for (token in tokens) if (!identified.contains(token)) identified += token

      item.set(ScriptorDataComponents.IDENTIFIED, identified)

      carried.shrink(1)
      player.cooldowns.addCooldown(carried.item, 10)
    }
  }

  data class CreativeEnchant(val slot: Int, val item: ItemStack)

  val creativeEnchant =
    NetworkManager.registerC2S(location("server_cursor_use_bookc"), CreativeEnchant::class) { payload, player ->
      val text = payload.item.get(DataComponents.WRITTEN_BOOK_CONTENT) ?: return@registerC2S
      computeIfAbsent((player.level() as ServerLevel)).parseComponents(decodeText(text))?.filterNotNull()?.let {
        ScriptorNetworkS2C.creativeBook(ScriptorNetworkS2C.CreativeBook(it, payload.slot), listOf(player))
      }
    }

  data class Scroll(val hand: InteractionHand, val amount: Double)

  val scroll = NetworkManager.registerC2S(location("server_scroll_networkc"), Scroll::class) { payload, player ->
    val item = player.getItemInHand(payload.hand)
    if (item.item is BookOfBooks) {
      val book = item.get(ScriptorDataComponents.BOOK_OF_BOOKS) ?: return@registerC2S
      val list = book.items
      if (list.isEmpty()) return@registerC2S

      var slot = book.active
      slot += sign(payload.amount).toInt() + list.size
      slot %= list.size
      item.set(ScriptorDataComponents.BOOK_OF_BOOKS, BookOfBooksData(list, slot))
    }
  }

  data class UseBook(val slot: Int)

  val useBook = NetworkManager.registerC2S(location("server_cursor_use_book"), UseBook::class) { payload, player ->
    val level = player.level()
    val slot: Int = payload.slot
    val item = player.containerMenu.items[slot]
    val carried = player.containerMenu.carried

    if (carried.isEmpty) return@registerC2S

    if(level is ServerLevel)
      SpellbookHelper.castFromItem(
        carried,
        player,
        maxCost = (ScriptorConfig.TOME_MAX_COST.invoke() * 20),
        targetOverride = listOf(ItemTargetable(item, player)),
      )
//    val text = carried.get(DataComponents.WRITTEN_BOOK_CONTENT)
//    if (text != null && level is ServerLevel) {
//      val spell = computeIfAbsent(level).parse(decodeText(text)) ?: return@registerC2S
//      if (spell.subject is InventorySubject) {
//        (spell.subject as InventorySubject).castOnItem(spell, player, item)
//        player.cooldowns.addCooldown(carried.item, (spell.cost() * 7).roundToInt())
//      }
//    }
  }

  fun register() {
    TraceNetwork
    WritingTableNetwork
  }
}