package com.ssblur.scriptor.network.server

import com.ssblur.scriptor.ScriptorMod.location
import com.ssblur.scriptor.blockentity.WritingTableBlockEntity
import com.ssblur.scriptor.data.components.DictionaryData
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.helpers.LimitedBookSerializer
import com.ssblur.scriptor.screen.menu.WritingTableMenu
import com.ssblur.unfocused.network.NetworkManager
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.WritableBookContent

object WritingTableNetwork {
    data class DictionaryMessage(val key: String, val value: String)
    val writeDictionaryEntry = NetworkManager.registerC2S(location("write_dictionary"), DictionaryMessage::class) { payload, player ->
        val menu = player.containerMenu
        if(menu is WritingTableMenu && menu.stillValid(player)) {
            val item = menu.dictionary
            val data = item[ScriptorDataComponents.DICTIONARY_DATA]
            var entries: MutableList<List<String>> = mutableListOf()
            data?.let { entries.addAll(it.values) }
            entries = entries.filter { it[0] != payload.key }.toMutableList()
            entries.add(listOf(payload.key, payload.value))
            item[ScriptorDataComponents.DICTIONARY_DATA] = DictionaryData(entries)
        }
    }

    data class BookMessage(
        val text: String,
        val pos: BlockPos,
        val slot: Int = 0,
        val held: Boolean = false,
        val container: Boolean = false,
        val append: Boolean = false
    )
    val writeBook = NetworkManager.registerC2S(location("write_book"), BookMessage::class) { (text, pos, slot, held, container, append), player ->
        val entity = player.level().getBlockEntity(pos)
        if(entity is WritingTableBlockEntity && player.position().distanceTo(pos.center) < player.attributes.getValue(Attributes.BLOCK_INTERACTION_RANGE)) {
            val item: ItemStack =
                if(container) entity.book
                else if (held) player.containerMenu.carried
                else player.inventory.getItem(slot)
            val content = LimitedBookSerializer.encodeTextWritable(text)
            if(append) {
                val existing = item[DataComponents.WRITABLE_BOOK_CONTENT]?.pages()?.toMutableList() ?: mutableListOf()
                existing.addAll(content)
                item[DataComponents.WRITABLE_BOOK_CONTENT] = WritableBookContent(existing)
            } else {
                item[DataComponents.WRITABLE_BOOK_CONTENT] = WritableBookContent(content)
            }
        }
    }

    fun write(text: String, pos: BlockPos, slot: Int = 0, held: Boolean = false, container: Boolean = false) {
        val split = text.split(' ')
        val size = 220
        var chunk = ""
        var first = true
        for(word in split) {
            if(word.length > size) {
                if(!chunk.isEmpty()) {
                    writeBook(BookMessage(chunk, pos, slot, held, container, !first))
                    first = false
                }
                var cut = word
                while(cut.length > size) {
                    writeBook(BookMessage(cut.substring(0..size), pos, slot, held, container, !first))
                    first = false
                    cut = cut.substring(size)
                }
                chunk = cut
            } else if((chunk.length + word.length) > size) {
                writeBook(BookMessage(chunk, pos, slot, held, container, !first))
                first = false
                chunk = word
            } else {
                chunk += " $word"
            }
        }
        writeBook(BookMessage(chunk, pos, slot, held, container, !first))
    }
}