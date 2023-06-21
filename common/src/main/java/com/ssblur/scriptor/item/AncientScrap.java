package com.ssblur.scriptor.item;

import com.ssblur.scriptor.events.ScrapReloadListener;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AncientScrap extends Item {
  int tier;
  public AncientScrap(Properties properties, int tier) {
    super(properties);
    this.tier = tier;
  }

  @Override
  public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
    super.appendHoverText(itemStack, level, list, tooltipFlag);
    list.add(Component.translatable("extra.scriptor.scrap_description"));
    list.add(Component.translatable("extra.scriptor.scrap_tier", tier));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
    var result = super.use(level, player, interactionHand);

    if(!level.isClientSide) {
      ServerLevel server = (ServerLevel) level;

      player.sendSystemMessage(Component.translatable("extra.scriptor.scrap_use"));
      player.getCooldowns().addCooldown(this, 20);

      // Generate and distribute scrap
      var scrap = ScrapReloadListener.INSTANCE.getRandomScrap(tier, player);


      if(!player.addItem(scrap)) {
        ItemEntity entity = new ItemEntity(
          level,
          player.getX(),
          player.getY() + 1,
          player.getZ() + 1,
          scrap
        );
        level.addFreshEntity(entity);
      }
      player.sendSystemMessage(Component.translatable("extra.scriptor.scrap_get"));
      player.getItemInHand(interactionHand).shrink(1);
      return InteractionResultHolder.consume(player.getItemInHand(interactionHand));
    }

    return result;
  }
}
