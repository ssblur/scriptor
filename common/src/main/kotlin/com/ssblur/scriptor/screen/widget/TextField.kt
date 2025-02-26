package com.ssblur.scriptor.screen.widget

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.client.gui.narration.NarratableEntry.NarrationPriority
import net.minecraft.client.gui.narration.NarratedElementType
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FormattedText
import net.minecraft.network.chat.Style
import java.util.*
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

class TextField(
    var x: Int,
    var y: Int,
    val w: Int,
    val h: Int,
    var editable: Boolean = true,
    var multiline: Boolean = true
): GuiEventListener, NarratableEntry, Renderable {
    data class CursorPos(val x: Int, val y: Int, val i: Int)
    private var textInternal: String = ""
    var text: String
        get() = textInternal
        set(value) {
            if(value != textInternal) lines = null
            textInternal = value
        }
    var cursor = CursorPos(0, 0, 0)
    var focus: Boolean = false
    var font: Font? = null
    var scrollOffset = 0
    override fun setFocused(bl: Boolean) {
        focus = bl
    }

    override fun mouseClicked(d: Double, e: Double, i: Int): Boolean {
        val clickX = d - x
        val clickY = e - y
        if(clickX > 0 && clickY > 0 && clickX < w && clickY < h)
            font?.let {
                cursor = getIndexAtPosition(it, clickX + 2, clickY)
                return true
            }
        return super.mouseClicked(d, e, i)
    }

    private var lines: List<FormattedText>? = null
    private var lineStartIndices: List<Int> = listOf(0)
    fun getIndexAtPosition(font: Font, clickX: Double, clickY: Double): CursorPos {
        Objects.requireNonNull(font)
        val i = floor(clickY / font.lineHeight).toInt() + scrollOffset
        val lines = splitLines()
        if (i < 0) {
            return CursorPos(0, 0, 0)
        } else if (i >= lines.size) {
            if(lines.size > 0)
                return CursorPos(lines.last().string.length, lines.size - 1, this.text.length)
            return CursorPos(0, 0, 0)
        } else {
            val line = lines[i]
            val x = font.splitter.plainIndexAtWidth(line.string, floor(clickX).toInt(), Style.EMPTY)
            return CursorPos(x, i, lineStartIndices[i] + x)
        }
    }

    private fun splitLines(): List<FormattedText> {
        if(lines == null)
            if(multiline) {
                lines = font?.splitter?.splitLines(FormattedText.of(text), w, Style.EMPTY)
                lines?.let {
                    val starts = mutableListOf<Int>()
                    var total = 0
                    for (line in it) {
                        starts.add(total)
                        total += line.string.length
                        while(total < text.length && text[total].isWhitespace())
                            total++
                    }
                    lineStartIndices = starts
                }
            } else {
                lineStartIndices = listOf(0)
                lines = listOf(FormattedText.of(text))
            }
        return lines ?: listOf()
    }

    fun moveCursorToIndex(i: Int) {
        if(i <= 0 || splitLines().isEmpty()) cursor = CursorPos(0, 0, 0)
        if(i > text.length) {
            moveCursorToIndex(text.length)
            return
        }

        for(line in 0..lineStartIndices.size-2) {
            if(lineStartIndices[line+1] > i) {
                cursor = CursorPos(i - lineStartIndices[line], line, i)
                return
            }
        }
        cursor = CursorPos(i - lineStartIndices.last(), lineStartIndices.size - 1, i)
    }

    fun moveCursorToY(i: Int) {
        if(i < 0 || i >= splitLines().size) return
        if(i < scrollOffset) scrollOffset = i
        if(i > scrollOffset + 7) scrollOffset = i - 7

        var x = min(splitLines()[i].string.length, cursor.x)
        x = max(0, x)
        cursor = CursorPos(x, i, lineStartIndices[i] + x)
    }

    fun moveCursorToX(i: Int) {
        if(i < 0) return
        var x = min(splitLines()[cursor.y].string.length, i)
        x = max(x, 0)
        cursor = CursorPos(x, cursor.y, lineStartIndices[cursor.y] + x)
    }

    override fun isFocused(): Boolean = focus

    override fun updateNarration(narrationElementOutput: NarrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, text)
    }
    override fun narrationPriority(): NarrationPriority = NarrationPriority.FOCUSED
    override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        font?.let { font ->
            if(editable && (Minecraft.getInstance().level?.gameTime ?: 0) % 40 > 20) {
                try {
                    val cy = (cursor.y - scrollOffset) * font.lineHeight + y
                    if((cursor.y - scrollOffset) >= 0 && (cursor.y - scrollOffset) <  8)
                        lines?.let { lines ->
                            val cx = font.splitter.stringWidth(lines[cursor.y].string.substring(0, cursor.x)).toInt() + x - 1
                            if(multiline && focus) guiGraphics.drawString(font, "|", cx, cy, 0xaaaaaa, false)
                        }
                } catch (_: StringIndexOutOfBoundsException) {
                } catch(_: IndexOutOfBoundsException) {}
            }
            var cy = y
            var skip = scrollOffset
            for(line in splitLines()){
                if(skip > 0) {
                    skip--
                    continue
                }
                if((cy + 2*font.lineHeight) < (y + h) || !multiline)
                    guiGraphics.drawString(font, line.string, x, cy, 0x0000000, false)
                else {
                    guiGraphics.drawString(font, Component.translatable("extra.scriptor.scroll"), x, cy, 0x777777, false)
                    break
                }
                cy += font.lineHeight
            }
        }
    }

    override fun mouseScrolled(d: Double, e: Double, f: Double, g: Double): Boolean {
        if(multiline) {
            scrollOffset -= g.sign.toInt()
            if (splitLines().size >= 2)
                scrollOffset = scrollOffset.coerceIn(0, splitLines().size - 1)
            else
                scrollOffset = 0
        }
        return super.mouseScrolled(d, e, f, g)
    }

    override fun keyPressed(i: Int, j: Int, k: Int): Boolean {
        if(editable && isFocused)
            when(i) {
                263 -> {
                    moveCursorToIndex(cursor.i - 1)
                    return true
                }
                262 -> {
                    moveCursorToIndex(cursor.i + 1)
                    return true
                }
                265 -> {
                    if(multiline) moveCursorToY(cursor.y - 1)
                    return true
                }
                264 -> {
                    if(multiline) moveCursorToY(cursor.y + 1)
                    return true
                }
                268 -> {
                    moveCursorToX(0)
                    return true
                }
                269 -> {
                    moveCursorToX(255)
                    return true
                }
                259 -> {
                    try {
                        text = text.substring(0, cursor.i - 1) + text.substring(cursor.i, text.length)
                        moveCursorToIndex(cursor.i - 1)
                    } catch(_: StringIndexOutOfBoundsException) {}
                    return true
                }
                261 -> {
                    try {
                        text = text.substring(0, cursor.i) + text.substring(cursor.i + 1, text.length)
                        moveCursorToIndex(cursor.i)
                    } catch(_: StringIndexOutOfBoundsException) {}
                    return true
                }
                69 -> return true
            }
        return super.keyPressed(i, j, k)
    }

    override fun keyReleased(i: Int, j: Int, k: Int): Boolean {
        return super.keyReleased(i, j, k)
    }

    override fun charTyped(c: Char, i: Int): Boolean {
        if(editable && isFocused) {
            if(cursor.i > text.length) moveCursorToIndex(text.length)
            text = text.substring(0, cursor.i) + c + text.substring(cursor.i, text.length)
            moveCursorToIndex(cursor.i + 1)
            if (cursor.y > scrollOffset + 7) scrollOffset = cursor.y - 7
        }
        return super.charTyped(c, i)
    }
}