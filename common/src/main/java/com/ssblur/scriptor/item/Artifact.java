package com.ssblur.scriptor.item;

import com.ssblur.scriptor.data.DictionarySavedData;
import com.ssblur.scriptor.helpers.targetable.SpellbookTargetable;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Artifact extends Item {
  public Artifact(Properties properties) {
    super(properties.stacksTo(1));
  }

  @Override
  public Component getName(ItemStack itemStack) {
    CompoundTag tag = itemStack.getTagElement("scriptor");
    if (tag != null) {
      String string = tag.getString("title");
      if (!StringUtil.isNullOrEmpty(string)) {
        return Component.literal(string);
      }
    }
    return super.getName(itemStack);
  }

  @Override
  public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
    super.appendHoverText(itemStack, level, list, tooltipFlag);

    list.add(Component.translatable("lore.scriptor.artifact_1").withStyle(ChatFormatting.GRAY));

    var tag = itemStack.getTagElement("scriptor");
    if(tag != null)
      list.add(Component.translatable("lore.scriptor.artifact_2", tag.getString("spell")));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
    var result = super.use(level, player, interactionHand);

    var itemStack = player.getItemInHand(interactionHand);
    var tag = itemStack.getTagElement("scriptor");
    if(tag == null || !(level instanceof ServerLevel server))
      return result;

    level.playSound(null, player.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 0.4F, level.getRandom().nextFloat() * 1.2F + 0.6F);

    var text = tag.getString("spell");
    Spell spell = DictionarySavedData.computeIfAbsent(server).parse(text);
    System.out.println(tag);
    if(spell != null) {
      spell.cast(new SpellbookTargetable(itemStack, player, player.getInventory().selected).withTargetItem(false));
      if(!player.isCreative())
        player.getCooldowns().addCooldown(itemStack.getItem(), (int) Math.round(spell.cost() * 2));
      return InteractionResultHolder.pass(itemStack);
    }
    return InteractionResultHolder.fail(itemStack);
  }
}
