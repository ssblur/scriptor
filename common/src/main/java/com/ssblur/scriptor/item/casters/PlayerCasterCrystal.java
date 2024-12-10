package com.ssblur.scriptor.item.casters;

import com.ssblur.scriptor.data.components.ScriptorDataComponents;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

public class PlayerCasterCrystal extends CasterCrystal {
  public PlayerCasterCrystal(Properties properties) {
    super(properties);
  }

  @Override
  public List<Targetable> getTargetables(ItemStack itemStack, Level level) {
    var uuid = itemStack.get(ScriptorDataComponents.PLAYER_FOCUS);
    if(uuid != null){
      var player = level.getPlayerByUUID(UUID.fromString(uuid));
      if(player != null)
        return List.of(new EntityTargetable(player));
    }
    return List.of();
  }

  @Override
  public void appendHoverText(ItemStack itemStack, TooltipContext context, List<Component> list, TooltipFlag tooltipFlag) {
    super.appendHoverText(itemStack, context, list, tooltipFlag);

    list.add(Component.translatable("lore.scriptor.player_crystal_1").withStyle(ChatFormatting.GRAY));

    var name = itemStack.get(ScriptorDataComponents.PLAYER_NAME);
    if(name != null) {
      list.add(Component.translatable(
        "lore.scriptor.player_crystal_2",
        name
      ).withStyle(ChatFormatting.GRAY));
      list.add(Component.translatable("lore.scriptor.crystal_reset").withStyle(ChatFormatting.GRAY));
    }
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
    var result = super.use(level, player, interactionHand);

    if(!level.isClientSide) {
      ItemStack itemStack = player.getItemInHand(interactionHand);
      itemStack.set(ScriptorDataComponents.PLAYER_FOCUS, player.getStringUUID());
      itemStack.set(ScriptorDataComponents.PLAYER_NAME, player.getDisplayName().getString());
    }

    return result;
  }
}
