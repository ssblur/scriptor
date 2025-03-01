package com.ssblur.scriptor.word.subject

import com.ssblur.scriptor.api.word.Subject
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.ItemTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.Spell
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import java.util.concurrent.CompletableFuture

class ImbueSubject: Subject(), InventorySubject {
  override fun getTargets(caster: Targetable, spell: Spell): CompletableFuture<List<Targetable>> {
    if (caster is EntityTargetable && caster.targetEntity is Player) {
      (caster.targetEntity as Player).sendSystemMessage(Component.translatable("extra.scriptor.enchant_wrong"))
    }
    val future = CompletableFuture<List<Targetable>>()
    future.complete(listOf())
    return future
  }

  override fun cost() = Cost(10.0, COSTTYPE.MULTIPLICATIVE)
  override fun castOnItem(spell: Spell, player: Player, slot: ItemStack) {
    spell.cast(EntityTargetable(player), ItemTargetable(slot, player))
  }
  override fun canBeCastOnInventory() = true
}
