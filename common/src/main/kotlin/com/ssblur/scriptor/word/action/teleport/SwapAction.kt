package com.ssblur.scriptor.word.action.teleport

import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.ItemTargetableHelper
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.TicketType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.RelativeMovement
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level

open class SwapAction : Action() {
  override fun apply(
    caster: Targetable,
    targetable: Targetable,
    descriptors: Array<Descriptor>,
    spellData: MutableList<String>
  ) {
    if (targetable.level.isClientSide) return

    teleport(caster, targetable)
    teleport(targetable, caster)
  }

  protected fun teleport(from: Targetable, to: Targetable) {
    val item = ItemTargetableHelper.getTargetItemStack(from)
    if(!item.isEmpty) {
      ItemTargetableHelper.depositItemStack(to, item.copy())
      item.count = 0
      return
    }

    if(from is EntityTargetable && from.targetEntity is LivingEntity) {
      val living = from.targetEntity as LivingEntity
      val level: Level = living.level()
      if (level !== to.level) {
        val toLevel = to.level as ServerLevel
        toLevel.chunkSource.addRegionTicket<Int>(
          TicketType.POST_TELEPORT,
          ChunkPos(to.targetBlockPos),
          1,
          living.getId()
        )
        living.teleportTo(
          toLevel,
          to.targetPos.x,
          to.targetPos.y,
          to.targetPos.z,
          setOf<RelativeMovement>(),
          living.getYRot(),
          living.getXRot()
        )
        living.stopRiding()
        if (living is Player) {
          if (living.isSleeping()) living.stopSleepInBed(true, true)
          living.onUpdateAbilities()
        }
      }

      if (living is PathfinderMob) living.getNavigation().stop()

      living.teleportTo(to.targetPos.x, to.targetPos.y, to.targetPos.z)
      living.setDeltaMovement(0.0, 0.0, 0.0)
      living.resetFallDistance()
    }
  }

  override fun cost() = Cost(6.0, COSTTYPE.ADDITIVE)
}
