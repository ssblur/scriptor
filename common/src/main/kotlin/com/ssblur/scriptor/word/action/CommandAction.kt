package com.ssblur.scriptor.word.action

import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.ItemTargetableHelper
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.InventoryTargetable
import com.ssblur.scriptor.helpers.targetable.ItemTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import net.minecraft.commands.CommandSource
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec2
import java.util.*

@Suppress("unused")
class CommandAction @JvmOverloads constructor(
  var cost: Double,
  blockTargetCommand: Array<String?> = arrayOfNulls(0),
  entityTargetCommand: Array<String?> = arrayOfNulls(0),
  itemTargetCommand: Array<String?> = arrayOfNulls(0)
): Action() {
  var entityTargetCommand: MutableList<String> = entityTargetCommand.filterNotNull().toMutableList()
  var blockTargetCommand: MutableList<String> = blockTargetCommand.filterNotNull().toMutableList()
  var itemTargetCommand: MutableList<String> = itemTargetCommand.filterNotNull().toMutableList()

  fun addEntityTargetCommand(entityTargetCommand: String) {
    this.entityTargetCommand.add(entityTargetCommand)
  }

  fun addItemTargetCommand(itemTargetCommand: String) {
    this.itemTargetCommand.add(itemTargetCommand)
  }

  fun addBlockTargetCommand(blockTargetCommand: String) {
    this.blockTargetCommand.add(blockTargetCommand)
  }

  override fun cost() = Cost.add(cost)

  override fun apply(caster: Targetable, targetable: Targetable, descriptors: Array<Descriptor>) {
    // @caster is a fake selector that's replaced at runtime.
    // It will target the entity or block position that cast this spell.
    // @target is a fake selector that is also replaced at runtime.
    // It will target the target of this spell, which is useful in item targeted commands.
    // This is run using execute.
    // By default, this is run as the target, with @s targeting them.
    var casterString = "@s"
    var tempCasterEntity: ArmorStand? = null
    var tempItemEntity: ArmorStand? = null

    if (caster is EntityTargetable) {
      val entity: Entity = caster.targetEntity
      casterString = getTargetSelector(entity)
    } else {
      val level = caster.level as ServerLevel
      tempCasterEntity = EntityType.ARMOR_STAND.spawn(level, caster.origin!!, MobSpawnType.COMMAND)
      if (tempCasterEntity != null) {
        casterString = getTargetSelector(tempCasterEntity)
        tempCasterEntity.customName = Component.translatable("block.scriptor.casting_lectern")
      }
    }

    val commandToParse: MutableList<String> = ArrayList()
    val pos = targetable.targetPos
    if (!ItemTargetableHelper.getTargetItemStack(targetable).isEmpty && !itemTargetCommand.isEmpty()) {
      var targetString = "@s"
      if (targetable is EntityTargetable) targetString = getTargetSelector(targetable.targetEntity)
      if (caster.level is ServerLevel) {
        val level = caster.level as ServerLevel
        tempItemEntity = EntityType.ARMOR_STAND.spawn(level, targetable.targetBlockPos, MobSpawnType.COMMAND)
        if (tempItemEntity != null) {
          tempItemEntity.setItemSlot(
            EquipmentSlot.MAINHAND,
            ItemTargetableHelper.getTargetItemStack(targetable).copy()
          )
          tempItemEntity.customName = Component.translatable("command.scriptor.magic_name")
          for (i in itemTargetCommand) commandToParse.add(
            String.format(
              "execute at %s as %s run %s",
              getTargetSelector(tempItemEntity),
              getTargetSelector(tempItemEntity),
              substituteTargetStrings(i, casterString, targetString)
            )
          )
        }
      } else {
        if (caster is EntityTargetable && caster.targetEntity is Player)
          (caster.targetEntity as Player).sendSystemMessage(Component.translatable("command.scriptor.no_item_commands_creative"))
      }
    } else if (targetable is EntityTargetable && entityTargetCommand.isNotEmpty()) {
      val entity: Entity = targetable.targetEntity
      val targetString = getTargetSelector(entity)
      for (i in entityTargetCommand) commandToParse.add(
        String.format(
          "execute at %s as %s run %s",
          targetString,
          targetString,
          substituteTargetStrings(i, casterString, targetString)
        )
      )
    } else {
      for (i in blockTargetCommand) commandToParse.add(
        String.format(
          Locale.US,
          "execute positioned %f %f %f in %s run %s",
          pos.x,
          pos.y,
          pos.z,
          targetable.level.dimension().location(),
          substituteTargetStrings(i, casterString, "@e[distance=..1]")
        )
      )
    }

    if (targetable.level.server == null) return
    val commands = targetable.level.server!!.commands
    for (command in commandToParse) {
      val results = commands.dispatcher.parse(
        command,
        CommandSourceStack(
          CommandSource.NULL,
          targetable.targetPos,
          Vec2(0f, 0f),
          targetable.level as ServerLevel,
          4,
          "Magic",
          Component.translatable("command.scriptor.magic_name"),
          targetable.level.server!!,
          null
        )
      )
      commands.performCommand(results, command)
    }

    tempCasterEntity?.remove(Entity.RemovalReason.DISCARDED)
    if (tempItemEntity != null) {
      if (targetable is InventoryTargetable) {
        if (targetable.container != null) targetable.container!!
          .setItem(targetable.targetedSlot, tempItemEntity.getItemBySlot(EquipmentSlot.MAINHAND))
      } else if (targetable is ItemTargetable) {
        targetable.targetItem.count = 0
        ItemTargetableHelper.depositItemStack(targetable, tempItemEntity.getItemBySlot(EquipmentSlot.MAINHAND))
      }
      tempItemEntity.remove(Entity.RemovalReason.DISCARDED)
    }
  }

  companion object {
    fun getTargetSelector(entity: Entity): String {
      val uuid = entity.uuid
      val l = uuid.leastSignificantBits
      val m = uuid.mostSignificantBits
      return String.format(
        "@e[nbt={UUID:[I;%d,%d,%d,%d]}]",
        (m shr 32).toInt(),
        m.toInt(),
        (l shr 32).toInt(),
        l.toInt()
      )
    }

    fun substituteTargetStrings(string: String, casterString: String, targetString: String): String {
      return string
        .replace("@caster[", casterString.substring(0, casterString.length - 1) + ",")
        .replace("@target[", targetString.substring(0, targetString.length - 1) + ",")
        .replace("@caster", casterString)
        .replace("@target", targetString)
    }
  }
}
