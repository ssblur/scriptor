package com.ssblur.scriptor.network.server

import com.ssblur.scriptor.ScriptorMod.location
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.network.client.ScriptorNetworkS2C.ExtendedTrace
import com.ssblur.scriptor.network.client.ScriptorNetworkS2C.Trace
import com.ssblur.scriptor.network.client.ScriptorNetworkS2C.extendedTrace
import com.ssblur.scriptor.network.client.ScriptorNetworkS2C.trace
import com.ssblur.unfocused.extension.ServerLevelExtension.runOnce
import com.ssblur.unfocused.network.NetworkManager
import io.netty.buffer.Unpooled
import net.minecraft.core.RegistryAccess
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.BlockHitResult
import java.util.*

object TraceNetwork {
  enum class TYPE { BLOCK, ENTITY, MISS }
  fun interface TraceCallback {
    fun run(target: Targetable)
  }

  data class TraceQueue(val player: Player, val callback: TraceCallback)

  private var queue: HashMap<UUID, TraceQueue> = HashMap()

  fun requestTraceData(player: Player, collideWithWater: Boolean, callback: (Targetable) -> Unit) {
    val uuid = UUID.randomUUID()
    val out = RegistryFriendlyByteBuf(Unpooled.buffer(), RegistryAccess.EMPTY)
    out.writeUUID(uuid)
    queue[uuid] = TraceQueue(player, callback)
    trace(Trace(uuid, collideWithWater), listOf(player))
  }

  fun requestExtendedTraceData(player: Player, collideWithWater: Boolean, callback: TraceCallback) {
    val uuid = UUID.randomUUID()
    val out = RegistryFriendlyByteBuf(Unpooled.buffer(), RegistryAccess.EMPTY)
    out.writeUUID(uuid)
    queue[uuid] = TraceQueue(player, callback)
    extendedTrace(ExtendedTrace(uuid, collideWithWater), listOf(player))
  }

  fun validateAndRun(uuid: UUID, player: Player, targetable: Targetable) {
    val queueItem = queue[uuid]
    if (queueItem!!.player === player) queueItem!!.callback.run(targetable)
  }

  fun validateAndDrop(uuid: UUID, player: Player) {
    val queueItem = queue[uuid]
    if (queueItem!!.player === player) queue.remove(uuid)
  }

  data class Payload(
    val uuid: UUID,
    val traceType: TYPE,
    val blockHitResult: BlockHitResult?,
    val entityId: Int,
    val entityUUID: UUID?
  )

  val returnTrace =
    NetworkManager.registerC2S(location("server_return_trace_data"), Payload::class) { payload, player ->
      when (payload.traceType) {
        TYPE.BLOCK -> {
          val result = payload.blockHitResult!!
          val targetable = Targetable(player.level(), result.location).setFacing(result.direction)

          player.serverLevel().runOnce { validateAndRun(payload.uuid, player, targetable) }
        }

        TYPE.ENTITY -> {
          val level = player.serverLevel()
          val entity = level.getEntity(payload.entityId)
          if (entity != null && entity.uuid == payload.entityUUID) level.runOnce {
            validateAndRun(payload.uuid, player, EntityTargetable(entity))
          }
          else level.runOnce { validateAndDrop(payload.uuid, player) }
        }

        else -> player.serverLevel().runOnce { validateAndDrop(payload.uuid, player) }
      }
    }
}