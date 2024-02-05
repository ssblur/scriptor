package com.ssblur.scriptor.item;

import com.ssblur.scriptor.ScriptorMod;
import com.ssblur.scriptor.helpers.ComponentHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Scrap extends Item {
  public Scrap(Properties properties) {
    super(properties);
  }

  @Override
  public Component getName(ItemStack itemStack) {
    CompoundTag compoundTag = itemStack.getTagElement("scriptor");
    if (compoundTag != null) {
      String string = compoundTag.getString("word");
      if (!StringUtil.isNullOrEmpty(string))
        return Component.literal(string);
    }
    return super.getName(itemStack);
  }

  @Override
  public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
    super.appendHoverText(itemStack, level, list, tooltipFlag);

    if(itemStack.getTagElement("scriptor") != null) {
      var scriptor = itemStack.getTagElement("scriptor");
      if(scriptor == null) return;
      var key = scriptor.getString("spell");
      if (key != null) {
        String[] parts = key.split(":", 2);
        if (parts.length == 2)
          ComponentHelper.updateTooltipWith(list, parts[0] + ".scriptor." + parts[1]);
        else
          ScriptorMod.LOGGER.error("Invalid Identify entry: " + key);
      }

      ComponentHelper.addCommunityDisclaimer(list, itemStack);
    }
  }
}
