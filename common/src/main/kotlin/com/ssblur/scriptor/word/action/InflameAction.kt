package com.ssblur.scriptor.word.action

import com.ssblur.scriptor.api.word.Action
import com.ssblur.scriptor.api.word.Descriptor
import com.ssblur.scriptor.helpers.DescriptorHelper.duration
import com.ssblur.scriptor.helpers.DescriptorHelper.strength
import com.ssblur.scriptor.helpers.ItemTargetableHelper
import com.ssblur.scriptor.helpers.targetable.EntityTargetable
import com.ssblur.scriptor.helpers.targetable.Targetable
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.BaseFireBlock
import kotlin.math.roundToInt

class InflameAction : Action() {
  override fun apply(
    caster: Targetable,
    targetable: Targetable,
    descriptors: Array<Descriptor>,
    spellData: MutableList<String>
  ) {
    val seconds = 2.0 + descriptors.duration()
    val strength = 1.0 + descriptors.strength()

    var exit = false
    (1..strength.roundToInt().coerceAtLeast(1)).forEach { _ ->
      val itemTarget = ItemTargetableHelper.getTargetItemStack(targetable)
      if (!itemTarget.isEmpty) {
        exit = true
        val check = RecipeManager.createCheck(RecipeType.SMELTING)
        val recipe = check.getRecipeFor(SingleRecipeInput(itemTarget.copy()), targetable.level)
        if (
          recipe.isPresent && recipe.get().value().ingredients.isNotEmpty() &&
          recipe.get().value().ingredients[0].items.isNotEmpty()
        ) {
          val count = recipe.get().value().ingredients[0].items[0].count
          itemTarget.shrink(count)
          ItemTargetableHelper.depositItemStack(
            targetable,
            recipe.get().value().getResultItem(targetable.level.registryAccess()).copy()
          )
        }
      }
    }
    if(exit) return

    if (targetable is EntityTargetable) {
      targetable.targetEntity.remainingFireTicks = Math.round(seconds * 20).toInt()
    } else {
      val pos = targetable.targetBlockPos
      val level = targetable.level

      if (!level.getBlockState(pos).canBeReplaced()) return

      val blockState2 = BaseFireBlock.getState(level, pos)
      level.setBlock(pos, blockState2, 11)

      if (caster is EntityTargetable && caster.targetEntity is Player) level.playSound(
        null,
        pos,
        SoundEvents.FLINTANDSTEEL_USE,
        SoundSource.BLOCKS,
        1.0f,
        level.getRandom().nextFloat() * 0.4f + 0.8f
      )
      else level.playSound(
        null,
        pos,
        SoundEvents.FLINTANDSTEEL_USE,
        SoundSource.BLOCKS,
        1.0f,
        level.getRandom().nextFloat() * 0.4f + 0.8f
      )
    }
  }

  override fun cost() = Cost(2.0, COSTTYPE.ADDITIVE)
}
