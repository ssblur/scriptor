package com.ssblur.scriptor.item.casters;

import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class PlayerCasterCrystal extends CasterCrystal {
  public PlayerCasterCrystal(Properties properties) {
    super(properties);
  }

  @Override
  public List<Targetable> getTargetables(ItemStack itemStack, Level level) {
    if(itemStack.getTag() != null && itemStack.getTag().contains("playerUUID")){
      var player = level.getPlayerByUUID(UUID.fromString(itemStack.getTag().getString("playerUUID")));
      if(player != null)
        return List.of(new EntityTargetable(player));
    }
    return List.of();
  }

  @Override
  public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
    super.appendHoverText(itemStack, level, list, tooltipFlag);

    list.add(Component.translatable("lore.scriptor.player_crystal"));
  }
}
