package com.ssblur.scriptor.word.subject

import com.ssblur.scriptor.api.word.Subject
import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.Spell
import net.minecraft.core.Direction
import java.util.concurrent.CompletableFuture

class SelfSubject: Subject() {
  override fun cost() = Cost(1.0, COSTTYPE.ADDITIVE)

  override fun getTargets(caster: Targetable, spell: Spell): CompletableFuture<List<Targetable>> {
    val result = CompletableFuture<List<Targetable>>()
    result.complete(listOf(caster.finalTargetable!!.setFacing(Direction.DOWN)))
    return result
  }
}
