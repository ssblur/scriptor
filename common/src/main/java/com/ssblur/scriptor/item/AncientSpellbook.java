package com.ssblur.scriptor.item;

import com.ssblur.scriptor.events.TomeReloadListener;
import com.ssblur.scriptor.helpers.DictionarySavedData;
import com.ssblur.scriptor.helpers.LimitedBookSerializer;
import com.ssblur.scriptor.helpers.TomeResource;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class AncientSpellbook extends Item {
  public AncientSpellbook(Properties properties) {
    super(properties.stacksTo(1));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
    var result = super.use(level, player, interactionHand);

    if(!level.isClientSide) {
      ServerLevel server = (ServerLevel) level;

      player.sendSystemMessage(Component.translatable("extra.scriptor.tome_use"));
      player.getCooldowns().addCooldown(this, 20);

      var resource = TomeReloadListener.INSTANCE.getRandomTome();
      Spell spell = resource.getSpell();
      String sentence = DictionarySavedData.computeIfAbsent(server).generate(spell);
      player.setItemInHand(interactionHand, LimitedBookSerializer.createSpellbook(resource.getAuthor(), resource.getName(), sentence));
      return InteractionResultHolder.consume(player.getItemInHand(interactionHand));
    }

    return result;
  }
}
