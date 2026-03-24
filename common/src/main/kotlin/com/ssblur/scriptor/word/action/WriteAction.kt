package com.ssblur.scriptor.word.action

import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.block.ScriptorBlocks
import com.ssblur.scriptor.blockentity.ScriptorBlockEntities
import com.ssblur.scriptor.helpers.targetable.Targetable

class WriteAction: Action() {
  override fun cost() = Cost(0.2, COSTTYPE.ADDITIVE)

  override fun apply(caster: Targetable, targetable: Targetable, descriptors: Array<Descriptor>, spellData: MutableList<String>) {
    val pos = targetable.targetBlockPos
    val level = targetable.level

    if (!level.getBlockState(pos).canBeReplaced()) return

    val state = ScriptorBlocks.CHALK.get().defaultBlockState()
    level.setBlockAndUpdate(pos, state)

    val entity = ScriptorBlockEntities.CHALK.get().create(pos, state) ?: return
    entity.word = spellData.removeFirst()
    entity.facing = targetable.facing
    level.setBlockEntity(entity)
  }
}
