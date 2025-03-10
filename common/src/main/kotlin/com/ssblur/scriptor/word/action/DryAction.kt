package com.ssblur.scriptor.word.action

import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.ItemTargetableHelper
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BucketItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.BucketPickup
import net.minecraft.world.level.block.LiquidBlock

class DryAction: Action() {
  override fun apply(caster: Targetable, targetable: Targetable, descriptors: Array<Descriptor>) {
    if (targetable.level.isClientSide) return

    val itemTarget = ItemTargetableHelper.getTargetItemStack(
      targetable,
      false
    ) { !it.isEmpty && it.isDamageableItem }
    if (!itemTarget.isEmpty) {
      if(itemTarget.item is BucketItem) {
        itemTarget.shrink(1)
        ItemTargetableHelper.depositItemStack(caster, ItemStack(Items.BUCKET))
      }
    }

    val pos = targetable.offsetBlockPos
    val state = targetable.level.getBlockState(pos)
    if (state.block.defaultDestroyTime() < 0) return
    val level = targetable.level as ServerLevel

    val block = state.block
    if(block is LiquidBlock) {
      level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())
    } else if(block is BucketPickup) {
      if (caster is EntityTargetable && caster.targetEntity is Player) {
        block.pickupBlock(caster.targetEntity as Player, level, pos, state)
      } else {
        block.pickupBlock(null, level, pos, state)
      }
    }
  }

  override fun cost() = Cost(0.6, COSTTYPE.ADDITIVE)
}
