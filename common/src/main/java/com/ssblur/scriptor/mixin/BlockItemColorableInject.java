package com.ssblur.scriptor.mixin;

import com.ssblur.scriptor.color.interfaces.ColorableBlock;
import com.ssblur.scriptor.color.interfaces.ColorableItem;
import com.ssblur.scriptor.registry.colorable.ColorableBlockRegistry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockItem.class)
public class BlockItemColorableInject implements ColorableItem {
  @Override
  public ItemStack setColor(int color, ItemStack itemStack) {
    var self = (BlockItem) (Object) this;
    if(self.getBlock() instanceof ColorableBlock colorable) {
      return colorable.setColor(color, itemStack);
    } else if(ColorableBlockRegistry.INSTANCE.has(self.getBlock())) {
      return ColorableBlockRegistry.INSTANCE.get(self.getBlock()).setColor(color, itemStack);
    }
    return ItemStack.EMPTY;
  }
}
