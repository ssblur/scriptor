package com.ssblur.scriptor.network.server

import com.ssblur.scriptor.ScriptorMod.location
import com.ssblur.scriptor.events.network.client.ClientExtendedTraceNetwork
import com.ssblur.scriptor.events.network.client.ClientTraceNetwork
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.unfocused.network.NetworkManager
import io.netty.buffer.Unpooled
import net.minecraft.core.RegistryAccess
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.BlockHitResult
import java.util.*

object TraceNetwork {
    enum class TYPE {BLOCK, ENTITY, MISS}
    fun interface TraceCallback {
        fun run(target: Targetable)
    }
    data class TraceQueue(val player: Player, val callback: TraceCallback)
    var queue: HashMap<UUID, TraceQueue> = HashMap()

    fun requestTraceData(player: Player, callback: (Targetable) -> Unit) {
        val uuid = UUID.randomUUID()
        val out = RegistryFriendlyByteBuf(Unpooled.buffer(), RegistryAccess.EMPTY)
        out.writeUUID(uuid)
        queue[uuid] = TraceQueue(player, callback)
        dev.architectury.networking.NetworkManager.sendToPlayer(player as ServerPlayer, ClientTraceNetwork.Payload(uuid))
    }

    fun requestExtendedTraceData(player: Player, callback: TraceCallback) {
        val uuid = UUID.randomUUID()
        val out = RegistryFriendlyByteBuf(Unpooled.buffer(), RegistryAccess.EMPTY)
        out.writeUUID(uuid)
        queue[uuid] = TraceQueue(player, callback)
        dev.architectury.networking.NetworkManager.sendToPlayer(player as ServerPlayer, ClientExtendedTraceNetwork.Payload(uuid))
    }

    fun validateAndRun(uuid: UUID, player: Player, targetable: Targetable) {
        val queueItem = queue[uuid]
        if (queueItem!!.player === player) queueItem!!.callback.run(targetable)
    }

    fun validateAndDrop(uuid: UUID, player: Player) {
        val queueItem = queue[uuid]
        if (queueItem!!.player === player) queue.remove(uuid)
    }

    data class Payload(val uuid: UUID, val traceType: TYPE, val blockHitResult: BlockHitResult?, val entityId: Int, val entityUUID: UUID?)
    val RETURN_TRACE_DATA = NetworkManager.registerC2S(
        location("server_return_trace_data"),
        Payload::class
    ) { payload, player ->
        val uuid: UUID = payload.uuid

        val type: TYPE = payload.traceType
        when (type) {
            TYPE.BLOCK -> {
                val result: BlockHitResult = payload.blockHitResult!!
                val pos = result.blockPos.relative(result.direction)
                val targetable = Targetable(player.level(), pos).setFacing(result.direction)

                player.serverLevel().server.addTickable { validateAndRun(uuid, player, targetable) }
            }

            TYPE.ENTITY -> {
                val entityId: Int = payload.entityId
                val entityUUID: UUID = payload.entityUUID!!
                val level = player.level()
                val entity = level.getEntity(entityId)
                if (entity != null && entity.uuid == entityUUID) player.serverLevel().server.addTickable {
                    validateAndRun(
                        uuid,
                        player,
                        EntityTargetable(entity)
                    )
                }
                else player.serverLevel().server.addTickable { validateAndDrop(uuid, player) }
            }

            else -> player.serverLevel().server.addTickable { validateAndDrop(uuid, player) }
        }
    }
}