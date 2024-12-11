package com.ssblur.scriptor.network.client

import com.ssblur.scriptor.ScriptorMod.location
import com.ssblur.scriptor.helpers.ParticleQueue.Companion.queue
import com.ssblur.scriptor.particle.MagicParticleData.Companion.magic
import com.ssblur.unfocused.network.NetworkManager
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.DustParticleOptions
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f
import java.util.*

object ParticleNetwork {
    enum class TYPE(val color: Vector3f) {
        FIZZLE(Vector3f(0.2f, 0.2f, 0.2f)),
        WITHER(Vector3f(0.1f, 0.1f, 0.1f)),
        MAGIC(Vector3f(0f, 0f, 0f))
    }
    data class Payload(val particleType: TYPE, val color: Int, val from: Vec3, val to: Vec3? = null)
    val send = NetworkManager.registerS2C(location("client_particle"), Payload::class) { payload: Payload ->
        val random = Random()
        when (val type = payload.particleType) {
            TYPE.FIZZLE, TYPE.WITHER ->
                for (i in 0..8)
                    queue(
                        DustParticleOptions(type.color, 1.0f),
                        payload.from.x() - 0.25f + random.nextFloat(1.5f),
                        payload.from.y() + 0.5f + random.nextFloat(0.7f),
                        payload.from.z() - 0.25f + random.nextFloat(1.5f)
                     )
            TYPE.MAGIC -> {
                val c = payload.color
                val r = (c and 0xff0000) shr 16
                val g = (c and 0x00ff00) shr 8
                val b = c and 0x0000ff

                for (i in 1..9) {
                    val pos = payload.from.lerp(payload.to!!, i / 10.0)
                    queue(magic(r, g, b), pos.x(), pos.y(), pos.z(), 0.0, 0.0, 0.0)
                }
            }
        }
    }

    fun fizzle(level: Level, pos: BlockPos) =
        send(Payload(TYPE.FIZZLE, 0, Vec3(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())), level.players())
    fun wither(level: Level, pos: BlockPos) =
        send(Payload(TYPE.WITHER, 0, Vec3(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())), level.players())
    fun magicTrail(level: Level, color: Int, from: Vec3, to: Vec3) =
        send(Payload(TYPE.WITHER, color, from, to), level.players())
}