package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.api.word.Action;
import com.ssblur.scriptor.api.word.Descriptor;
import com.ssblur.scriptor.helpers.ItemTargetableHelper;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.ItemTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.api.word.descriptor.DurationDescriptor;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;

public class InflameAction extends Action {
  @Override
  public void apply(Targetable caster, Targetable targetable, Descriptor[] descriptors) {
    double seconds = 2;
    for(var d: descriptors) {
      if(d instanceof DurationDescriptor durationDescriptor)
        seconds += durationDescriptor.durationModifier();
    }

    if(targetable instanceof ItemTargetable itemTargetable && itemTargetable.shouldTargetItem()) {
      var check = RecipeManager.createCheck(RecipeType.SMELTING);
      var recipe = check.getRecipeFor(new SingleRecipeInput(itemTargetable.getTargetItem()), itemTargetable.getLevel());
      if(recipe.isPresent() && !recipe.get().value().getIngredients().isEmpty() && recipe.get().value().getIngredients().get(0).getItems().length > 0) {
        int count = recipe.get().value().getIngredients().get(0).getItems()[0].getCount();
        itemTargetable.getTargetItem().shrink(count);

        var pos = itemTargetable.getTargetPos();
        ItemEntity entity = new ItemEntity(
          itemTargetable.getLevel(),
          pos.x(),
          pos.y() + 1,
          pos.z(),
          recipe.get().value().getResultItem(targetable.getLevel().registryAccess())
        );
        caster.getLevel().addFreshEntity(entity);
      }
      return;
    }

    var itemTarget = ItemTargetableHelper.getTargetItemStack(targetable);
    if(!itemTarget.isEmpty()) {
      var check = RecipeManager.createCheck(RecipeType.SMELTING);
      var recipe = check.getRecipeFor(new SingleRecipeInput(itemTarget), targetable.getLevel());
      if(recipe.isPresent() && recipe.get().value().getIngredients().size() > 0 && recipe.get().value().getIngredients().get(0).getItems().length > 0) {
        int count = recipe.get().value().getIngredients().get(0).getItems()[0].getCount();
        itemTarget.shrink(count);
        ItemTargetableHelper.depositItemStack(targetable, recipe.get().value().getResultItem(targetable.getLevel().registryAccess()));
        return;
      }
    }

    if(targetable instanceof EntityTargetable entityTargetable) {
      entityTargetable.getTargetEntity().setRemainingFireTicks((int) Math.round(seconds * 20));
    } else {
      BlockPos pos = targetable.getTargetBlockPos();
      Level level = targetable.getLevel();

      if(!level.getBlockState(pos).canBeReplaced())
        return;

      BlockState blockState2 = BaseFireBlock.getState(level, pos);
      level.setBlock(pos, blockState2, 11);

      if(caster instanceof EntityTargetable entityTargetable && entityTargetable.getTargetEntity() instanceof Player player)
        level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
      else
        level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
    }
  }

  @Override
  public Cost cost() { return new Cost(2, COSTTYPE.ADDITIVE); }

}
