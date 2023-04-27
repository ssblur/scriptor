package com.ssblur.scriptor.item.casters;

import com.ssblur.scriptor.events.messages.TraceNetwork;
import com.ssblur.scriptor.helpers.targetable.EntityTargetable;
import com.ssblur.scriptor.helpers.targetable.Targetable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
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

    list.add(Component.translatable("lore.scriptor.player_crystal_1").withStyle(ChatFormatting.GRAY));
    if(itemStack.getTag() != null && itemStack.getTag().contains("playerUUID")) {
      var uuid = itemStack.getTag().getString("playerUUID");
      if(level != null && level.getPlayerByUUID(UUID.fromString(uuid)) != null) {
        list.add(Component.translatable(
          "lore.scriptor.player_crystal_2",
          level.getPlayerByUUID(UUID.fromString(uuid)).getName()
        ).withStyle(ChatFormatting.GRAY));
      } else {
        list.add(Component.translatable("lore.scriptor.player_crystal_3"));
        list.add(Component.literal("(" + uuid + ")"));
      }
      list.add(Component.translatable("lore.scriptor.crystal_reset").withStyle(ChatFormatting.GRAY));
    }
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
    var result = super.use(level, player, interactionHand);

    if(!level.isClientSide) {
      ServerLevel server = (ServerLevel) level;

      ItemStack itemStack = player.getItemInHand(interactionHand);
      itemStack.getOrCreateTag().putString("playerUUID", player.getStringUUID());
    }

    return result;
  }
}
