package com.ssblur.scriptor.color

import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.word.descriptor.color.ColorDescriptor
import net.minecraft.client.Minecraft
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import java.awt.Color
import java.util.*
import java.util.function.Function
import kotlin.math.pow
import kotlin.math.sqrt

object CustomColors {
    var registry: HashMap<Int, Function<Float, Int>> = HashMap()
    var currentNumber: Int = 0
    var colors: HashMap<String, Int> = HashMap()

    fun reset() {
        currentNumber = 0
        colors = HashMap()
        registry = HashMap()

        // Rainbow
        // it can be gay too, as a treat.
        register("rainbow") { t: Float ->
            var tick = t
            var s = tick % 31
            s /= 93f
            s += 0.75f
            var b = tick % 23
            b /= 46f
            b += 0.5f
            tick %= 40f
            Color.getHSBColor(tick / 40f, s, b).rgb
        }
    }

    fun putColor(index: Int, key: String, colors: IntArray) {
        this.colors[key] = index
        registry[index] = createCustomColor(colors)
    }

    fun register(value: String, color: Function<Float, Int>): Int {
        currentNumber--
        colors[value] = currentNumber
        registry[currentNumber] = color
        return currentNumber
    }

    init {
        reset()
    }

    fun registerWithEasing(key: String, values: IntArray): Int {
        return register(
            key,
            createCustomColor(values)
        )
    }

    fun getKey(key: String): Int {
        return colors[key]!!
    }

    fun getColor(color: Int, tick: Float): Int {
        if (color > 0) return color
        if (registry.containsKey(color)) return registry[color]!!
            .apply(tick)
        return 0
    }

    fun getColor(descriptors: Iterable<Descriptor>): Int {
        var r: Long = 0
        var g: Long = 0
        var b: Long = 0
        var a: Long = 0
        var colorN = 0
        var c: Int
        for (d in descriptors) if (d is ColorDescriptor) if (d.color >= 0) {
            colorN += 1
            c = d.color
            b += (c and 0xFF).toLong()
            g += ((c shr 8) and 0xFF).toLong()
            r += ((c shr 16) and 0xFF).toLong()
            a += ((c shr 24) and 0xFF).toLong()
        } else {
            return d.color
        }
        if (colorN == 0) return 0xa020f0

        r /= colorN.toLong()
        g /= colorN.toLong()
        b /= colorN.toLong()
        a /= colorN.toLong()
        return ((a shl 24) + (r shl 16) + (g shl 8) + b).toInt()
    }

    fun getColor(descriptors: Array<Descriptor>?): Int {
        return getColor(Arrays.stream(descriptors).toList())
    }

    fun getColor(itemStack: ItemStack): Int {
        val tick = Minecraft.getInstance().level!!.gameTime
        val dye = itemStack.get(DataComponents.DYED_COLOR)
        if (dye != null) return -0x1000000 + getColor(dye.rgb(), tick.toFloat())
        return -0x5fdf10
    }

    fun ease(from: Int, to: Int, p: Float): Int {
        var partial = p
        partial = partial.pow(5.0f)
        val bA = from and 0xFF
        val gA = (from shr 8) and 0xFF
        val rA = (from shr 16) and 0xFF
        val aA = (from shr 24) and 0xFF
        val bB = to and 0xFF
        val gB = (to shr 8) and 0xFF
        val rB = (to shr 16) and 0xFF
        val aB = (to shr 24) and 0xFF

        val b = (bA * partial + bB * (1 - partial)).toInt()
        val g = (gA * partial + gB * (1 - partial)).toInt()
        val r = (rA * partial + rB * (1 - partial)).toInt()
        val a = (aA * partial + aB * (1 - partial)).toInt()
        return (a shl 24) + (r shl 16) + (g shl 8) + b
    }

    fun createCustomColor(list: IntArray): Function<Float, Int> {
        return Function { t: Float ->
            var tick = t
            val partial = tick % 60 / 60
            tick /= 60f
            tick %= list.size.toFloat()
            val index = tick.toInt()
            val color = list[index]
            val nextColor = list[(index + 1) % list.size]
            ease(nextColor, color, partial)
        }
    }

    fun getDyeColor(color: Int, tick: Float): DyeColor {
        var distance = -1
        var dyeColor = DyeColor.WHITE

        val c = getColor(color, tick)
        val b = c and 0xFF
        val g = (c shr 8) and 0xFF
        val r = (c shr 16) and 0xFF

        var cD: Int
        var bD: Int
        var gD: Int
        var rD: Int
        var d: Int
        for (dye in DyeColor.entries) {
            cD = dye.fireworkColor
            bD = cD and 0xFF
            gD = (cD shr 8) and 0xFF
            rD = (cD shr 16) and 0xFF
            d = sqrt((b - bD).toFloat().pow(2.0f) + (g - gD).toFloat().pow(2.0f) + (r - rD).toFloat().pow(2.0f)).toInt()
            if (d < distance || distance == -1) {
                dyeColor = dye
                distance = d
            }
        }

        return dyeColor
    }

    data class RGB(val r: Int, val g: Int, val b: Int)
    fun Int.splitIntoRGB(): RGB = RGB(this and 0xFF, (this shr 8) and 0xFF, (this shr 16) and 0xFF)
}
