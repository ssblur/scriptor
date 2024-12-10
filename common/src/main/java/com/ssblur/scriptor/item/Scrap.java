package com.ssblur.scriptor.item;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.data.components.ScriptorDataComponents;
import com.ssblur.scriptor.helpers.ComponentHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class Scrap extends Item {
  public Scrap(Properties properties) {
    super(properties);
  }

  @Override
  public void appendHoverText(ItemStack itemStack, TooltipContext level, List<Component> list, TooltipFlag tooltipFlag) {
    super.appendHoverText(itemStack, level, list, tooltipFlag);

    var key = itemStack.get(ScriptorDataComponents.SPELL);
    if (key != null) {
      String[] parts = key.split(":", 2);
      if (parts.length == 2)
        ComponentHelper.updateTooltipWith(list, parts[0] + ".scriptor." + parts[1]);
      else
        ScriptorMod.INSTANCE.getLOGGER().error("Invalid Identify entry: {}", key);
    }

    ComponentHelper.addCommunityDisclaimer(list, itemStack);
  }
}
