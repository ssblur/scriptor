package com.ssblur.scriptor.network.client

import com.ssblur.scriptor.ScriptorMod.location
import com.ssblur.scriptor.color.CustomColors.splitIntoRGB
import com.ssblur.scriptor.helpers.ParticleQueue.queue
import com.ssblur.scriptor.particle.MagicParticleData.Companion.magic
import com.ssblur.unfocused.network.NetworkManager
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.DustParticleOptions
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f
import java.util.*

object ParticleNetwork {
  enum class TYPE(val color: Vector3f) {
    FIZZLE(Vector3f(0.2f, 0.2f, 0.2f)),
    WITHER(Vector3f(0.1f, 0.1f, 0.1f)),
    MAGIC(Vector3f(0f, 0f, 0f)),
    MAGIC_SCATTER(Vector3f(0f, 0f, 0f))
  }

  data class Payload(val particleType: TYPE, val color: Int, val from: Vec3, val to: Vec3? = null)

  val send = NetworkManager.registerS2C(location("client_particle"), Payload::class) { payload: Payload ->
    val random = Random()
    when (val type = payload.particleType) {
      TYPE.FIZZLE, TYPE.WITHER ->
        (0..8).forEach { _ ->
          queue(
            DustParticleOptions(type.color, 1.0f),
            payload.from.x() - 0.25f + random.nextFloat(1.5f),
            payload.from.y() + 0.5f + random.nextFloat(0.7f),
            payload.from.z() - 0.25f + random.nextFloat(1.5f)
          )
        }

      TYPE.MAGIC -> {
        val c = payload.color
        val (r, g, b) = c.splitIntoRGB()

        val steps = 16.0
        for (i in 1..steps.toInt()) {
          val pos = payload.from.lerp(payload.to!!, i / steps)
          queue(magic(r, g, b), pos.x(), pos.y(), pos.z(), 0.0, 0.0, 0.0)
        }
      }

      TYPE.MAGIC_SCATTER -> {
        val c = payload.color
        val (r, g, b) = c.splitIntoRGB()

        val steps = 14
        (0..steps).forEach { _ ->
          val pos = payload.from.add(
            random.nextDouble(2.0) - 1.0,
            random.nextDouble(0.5) - 0.25,
            random.nextDouble(2.0) - 1.0
          )
          queue(magic(r, g, b), pos.x(), pos.y(), pos.z(), 0.0, 0.0, 0.0)
        }
      }
    }
  }

  fun fizzle(level: Level, pos: BlockPos) =
    send(Payload(
      TYPE.FIZZLE,
      0,
      Vec3(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())),
      nearbyPlayers(level, Vec3(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()))
    )

  fun wither(level: Level, pos: BlockPos) =
    send(Payload(
      TYPE.WITHER,
      0,
      Vec3(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())),
      nearbyPlayers(level, Vec3(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble()))
    )

  fun magicTrail(level: Level, color: Int, from: Vec3, to: Vec3) =
    send(Payload(TYPE.MAGIC, color, from, to), nearbyPlayers(level, from) + nearbyPlayers(level, to))

  fun magicScatter(level: Level, color: Int, pos: Vec3) =
    send(Payload(TYPE.MAGIC_SCATTER, color, pos, null), nearbyPlayers(level, pos))

  fun nearbyPlayers(level: Level, pos: Vec3): List<Player> =
    level.getNearbyPlayers(
      TargetingConditions.forNonCombat(),
      @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") null,
      AABB.ofSize(pos, 64.0, 64.0, 64.0)
    )

  fun init() {}
}