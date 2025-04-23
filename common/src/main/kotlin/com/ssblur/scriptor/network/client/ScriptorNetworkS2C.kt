package com.ssblur.scriptor.network.client

import com.ssblur.scriptor.ScriptorMod.COMMUNITY_MODE
import com.ssblur.scriptor.ScriptorMod.location
import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.api.word.Subject
import com.ssblur.scriptor.blockentity.PhasedBlockBlockEntity
import com.ssblur.scriptor.color.CustomColors.putColor
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.network.server.TraceNetwork.Payload
import com.ssblur.scriptor.network.server.TraceNetwork.TYPE
import com.ssblur.scriptor.network.server.TraceNetwork.returnTrace
import com.ssblur.scriptor.registry.words.WordRegistry.actionRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.descriptorRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.subjectRegistry
import com.ssblur.scriptor.word.PartialSpell
import com.ssblur.scriptor.word.Spell
import com.ssblur.scriptor.word.subject.InventorySubject
import com.ssblur.unfocused.network.NetworkManager
import net.minecraft.client.Minecraft
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import java.util.*

@Suppress("unused")
object ScriptorNetworkS2C {
  data class CreativeBook(val components: List<String>, val slot: Int)

  val creativeBook =
    NetworkManager.registerS2C(location("client_cursor_return_bookc"), CreativeBook::class) { payload ->
      var action: Action? = null
      var subject: Subject? = null
      val descriptor = arrayListOf<Descriptor>()
      for (i in payload.components) {
        val split = i.replace("\"", "").split(":".toRegex(), limit = 2).toTypedArray()

        when (split[0]) {
          "action" -> actionRegistry[split[1]]?.let { action = it }
          "descriptor" -> descriptorRegistry[split[1]]?.let { descriptor.add(it) }
          "subject" -> subjectRegistry[split[1]]?.let { subject = it }
        }
      }
      val spell = Spell(subject!!, PartialSpell(action!!, *descriptor.toTypedArray()))
      val player = Minecraft.getInstance().player!!
      if (spell.subject is InventorySubject) {
        (spell.subject as InventorySubject).castOnItem(spell, player, player.containerMenu.items[payload.slot])
        player.cooldowns.addCooldown(player.containerMenu.carried.item, 5)
      }
    }

  data class ExtendedTrace(val uuid: UUID, val collideWithWater: Boolean, val scale: Double = 20.0)

  fun extendedTraceCallback(payload: ExtendedTrace) {
    val player: Player = Minecraft.getInstance().player!!
    val level = player.level()
    val position = player.eyePosition
    val angle = player.lookAngle.normalize().multiply(payload.scale, payload.scale, payload.scale)
    val dest = angle.add(position)
    val blockHitResult =
      level.clip(ClipContext(
        position,
        dest,
        ClipContext.Block.COLLIDER,
        if(payload.collideWithWater) ClipContext.Fluid.ANY else ClipContext.Fluid.NONE,
        player
      ))

    val entityHitResult = ProjectileUtil.getEntityHitResult(
      level,
      player,
      position,
      dest,
      AABB.ofSize(position.subtract(0.1, 0.1, 0.1), 0.2, 0.2, 0.2).expandTowards(angle).inflate(1.0)
    ) { _ -> true }
    if (entityHitResult != null && entityHitResult.type != HitResult.Type.MISS) {
      if (blockHitResult.type != HitResult.Type.MISS && blockHitResult.distanceTo(player) < entityHitResult.distanceTo(
          player
        )
      )
        returnTrace(Payload(payload.uuid, TYPE.BLOCK, blockHitResult, 0, null))
      else
        returnTrace(
          Payload(
            payload.uuid,
            TYPE.ENTITY,
            blockHitResult,
            entityHitResult.entity.id,
            entityHitResult.entity.uuid
          )
        )
    } else if (blockHitResult.type != HitResult.Type.MISS)
      returnTrace(Payload(payload.uuid, TYPE.BLOCK, blockHitResult, 0, null))
    else
      returnTrace(Payload(payload.uuid, TYPE.MISS, blockHitResult, 0, null))
  }

  val extendedTrace = NetworkManager.registerS2C(location("client_get_hitscan_data"), ExtendedTrace::class, ::extendedTraceCallback)

  data class Trace(val uuid: UUID, val collideWithWater: Boolean)

  val trace = NetworkManager.registerS2C(location("client_get_touch_data"), Trace::class) { payload ->
    if(!payload.collideWithWater) {
      val hit = Minecraft.getInstance().hitResult
      when (Objects.requireNonNull<HitResult?>(hit).type) {
        HitResult.Type.BLOCK ->
          returnTrace(Payload(payload.uuid, TYPE.BLOCK, hit as BlockHitResult, 0, null))

        HitResult.Type.ENTITY -> {
          val entity = (hit as EntityHitResult).entity
          returnTrace(Payload(payload.uuid, TYPE.ENTITY, null, entity.id, entity.uuid))
        }

        else -> returnTrace(Payload(payload.uuid, TYPE.MISS, null, 0, null))
      }
    } else {
      val player = Minecraft.getInstance().player
      if(player != null)
        extendedTraceCallback(
          ExtendedTrace(
            payload.uuid,
            true,
            player.attributes.getValue(Attributes.BLOCK_INTERACTION_RANGE)
          )
        )
    }
  }

  data class Identify(val components: List<String>, val slot: Int)

  val identify = NetworkManager.registerS2C(location("client_cursor_return_scrollc"), Identify::class) { payload ->
    Minecraft.getInstance().player!!.containerMenu.getSlot(payload.slot).item.set(
      ScriptorDataComponents.IDENTIFIED,
      payload.components
    )
  }

  data class Color(val name: String, val index: Int, val r: Int, val g: Int, val b: Int)

  val color = NetworkManager.registerS2C(location("client_color_receivec"), Color::class) { payload ->
    putColor(payload.index, payload.name, intArrayOf(payload.r, payload.g, payload.b))
  }

  enum class FLAGS { COMMUNITY, INVERT_DO_NOT_PHASE }
  data class Flag(val key: FLAGS, val value: Boolean)

  val flag = NetworkManager.registerS2C(location("client_flag"), Flag::class) { payload ->
    when (payload.key) {
      FLAGS.COMMUNITY -> COMMUNITY_MODE = payload.value
      FLAGS.INVERT_DO_NOT_PHASE -> PhasedBlockBlockEntity.INVERT_DO_NOT_PHASE = payload.value
    }
  }

  fun register() {
    ParticleNetwork
  }
}