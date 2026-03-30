package com.ssblur.scriptor.item

import com.google.common.base.Predicate
import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.network.client.ScriptorNetworkS2C
import net.minecraft.core.component.DataComponents
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import kotlin.math.min
import kotlin.math.roundToInt

class Gun(val damage: Int, val ammo: Int, properties: Properties) : Item(properties) {
  var sounds: List<SoundEvent> = listOf()
  var ammoPredicate: Predicate<ItemStack>? = null
  var ammoCount = 6

  fun reload(level: Level, player: Player, item: ItemStack) {
    val ammo = item[DataComponents.DAMAGE] ?: 0
    if(ammoPredicate == null) {
      item[DataComponents.DAMAGE] = ammoCount
    } else {
      val match = player.inventory.items.firstOrNull { ammoPredicate!!.test(it) }
      if(match == null) {
        player.cooldowns.addCooldown(this, 5)
        level.playSound(null, player.blockPosition().above(), ScriptorMod.NO_AMMO_CLICK.get(), SoundSource.PLAYERS)
        return
      }
      val available = min(ammoCount - ammo, match.count)
      item[DataComponents.DAMAGE] = available + ammo
      match.shrink(available)
    }
    level.playSound(null, player.blockPosition().above(), ScriptorMod.GUN_RELOAD.get(), SoundSource.PLAYERS)
    player.cooldowns.addCooldown(this, 60)
  }

  override fun isBarVisible(itemStack: ItemStack): Boolean = true

  override fun getBarWidth(item: ItemStack): Int {
    if(item[DataComponents.DAMAGE] != null)
      return (13f * item[DataComponents.DAMAGE]!!/ammo).roundToInt()
    return 0
  }

  override fun use(
    level: Level,
    player: Player,
    interactionHand: InteractionHand
  ): InteractionResultHolder<ItemStack?>? {
    val item = player.getItemInHand(interactionHand)
    if(level.isClientSide) return InteractionResultHolder.sidedSuccess(item, true)

    val ammo = item[DataComponents.DAMAGE] ?: 0
    if(ammo <= 0 || player.isCrouching) {
      reload(level, player, item)
    } else {
      ScriptorNetworkS2C.requestGunTrace(
        ScriptorNetworkS2C.RequestGunTrace(interactionHand),
        listOf(player)
      )
      level.playSound(null, player.blockPosition().above(), ScriptorMod.GUN_BLAST.get(), SoundSource.PLAYERS)

      item[DataComponents.DAMAGE] = ammo - 1
    }

    return InteractionResultHolder.success(item)
  }
}