package com.ssblur.scriptor.helpers

import net.minecraft.client.Minecraft
import net.minecraft.core.particles.ParticleOptions

class ParticleQueue {
    @JvmRecord
    data class Entry(
        val particleOptions: ParticleOptions,
        val d: Double,
        val e: Double,
        val f: Double,
        val g: Double,
        val h: Double,
        val i: Double
    )

    var queue: MutableList<Entry> = ArrayList()

    fun process() {
        val level = Minecraft.getInstance().level
        while (level != null && !queue.isEmpty()) {
            val item = queue.removeAt(0)
            level.addParticle(item.particleOptions, item.d, item.e, item.f, item.g, item.h, item.i)
        }
    }

    companion object {
        @JvmField
        val INSTANCE: ParticleQueue = ParticleQueue()
        @JvmStatic
        @JvmOverloads
        fun queue(
            particleOptions: ParticleOptions,
            d: Double,
            e: Double,
            f: Double,
            g: Double = 0.0,
            h: Double = 0.0,
            i: Double = 0.0
        ) {
            INSTANCE.queue.add(Entry(particleOptions, d, e, f, g, h, i))
        }
    }
}
