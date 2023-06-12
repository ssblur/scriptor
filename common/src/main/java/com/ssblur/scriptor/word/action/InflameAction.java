package com.ssblur.scriptor.word.action;

import com.ssblur.scriptor.enchant.ChargedEnchant;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.InventoryTargetable;
import com.ssblur.scriptor.helpers.targetable.ItemTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import com.ssblur.scriptor.word.descriptor.Descriptor;
import com.ssblur.scriptor.word.descriptor.duration.DurationDescriptor;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
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
      var container = new SimpleContainer(1);
      container.addItem(itemTargetable.getTargetItem());
      var recipe = check.getRecipeFor(container, itemTargetable.getLevel());
      if(recipe.isPresent() && recipe.get().getIngredients().size() > 0 && recipe.get().getIngredients().get(0).getItems().length > 0) {
        int count = recipe.get().getIngredients().get(0).getItems()[0].getCount();
        itemTargetable.getTargetItem().shrink(count);

        var pos = itemTargetable.getTargetPos();
        ItemEntity entity = new ItemEntity(
          itemTargetable.getLevel(),
          pos.x(),
          pos.y() + 1,
          pos.z(),
          recipe.get().getResultItem(targetable.getLevel().registryAccess())
        );
        caster.getLevel().addFreshEntity(entity);
      }
      return;
    }

    if(targetable instanceof InventoryTargetable inventoryTargetable) {
      if(inventoryTargetable.getContainer() != null) {
        int slot;
        if(inventoryTargetable.shouldIgnoreTargetedSlot())
          slot = inventoryTargetable.getFirstFilledSlot();
        else
          slot = inventoryTargetable.getTargetedSlot();
        var itemStack = inventoryTargetable.getContainer().getItem(slot);
        if (itemStack != null) {
          var check = RecipeManager.createCheck(RecipeType.SMELTING);
          var container = new SimpleContainer(1);
          container.addItem(itemStack);
          var recipe = check.getRecipeFor(container, targetable.getLevel());
          if(recipe.isPresent() && recipe.get().getIngredients().size() > 0 && recipe.get().getIngredients().get(0).getItems().length > 0) {
            int count = recipe.get().getIngredients().get(0).getItems()[0].getCount();
            itemStack.shrink(count);

            var result = recipe.get().getResultItem(targetable.getLevel().registryAccess());
            count = result.getCount();
            slot = inventoryTargetable.getFirstMatchingSlotNotEmpty(result);
            if(slot >= 0) {
              var item = inventoryTargetable.getContainer().getItem(slot);
              if (item.getCount() + count < item.getMaxStackSize()) {
                item.grow(count);
                return;
              }
            }

            slot = inventoryTargetable.getFirstMatchingSlot(ItemStack::isEmpty);
            if(slot >= 0) {
              inventoryTargetable.getContainer().setItem(slot, result);
              return;
            }
            var pos = targetable.getTargetPos();
            ItemEntity entity = new ItemEntity(
              targetable.getLevel(),
              pos.x(),
              pos.y() + 1,
              pos.z(),
              recipe.get().getResultItem(targetable.getLevel().registryAccess())
            );
            caster.getLevel().addFreshEntity(entity);
          }
          return;
        }
      }
    }

    if(targetable instanceof EntityTargetable entityTargetable) {
      entityTargetable.getTargetEntity().setSecondsOnFire((int) Math.round(seconds));
    } else {
      BlockPos pos = targetable.getTargetBlockPos();
      Level level = targetable.getLevel();

      if(!level.getBlockState(pos).getMaterial().isReplaceable())
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
