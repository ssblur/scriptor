package com.ssblur.scriptor.helpers;

import com.ssblur.scriptor.ScriptorGameRules;
import com.ssblur.scriptor.advancement.ScriptorAdvancements;
import com.ssblur.scriptor.data.DictionarySavedData;
import com.ssblur.scriptor.helpers.targetable.SpellbookTargetable;
import com.ssblur.scriptor.word.Spell;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SpellbookHelper {
  public static List<Item> SPELLBOOKS = new ArrayList<>();

  public static boolean castFromItem(ItemStack itemStack, Player player) {
    var compound = itemStack.getTag();
    var level = player.level();
    if(compound == null || !(level instanceof ServerLevel server))
      return false;

    level.playSound(null, player.blockPosition(), SoundEvents.EVOKER_CAST_SPELL, SoundSource.PLAYERS, 0.4F, level.getRandom().nextFloat() * 1.2F + 0.6F);

    var text = compound.getList("pages", Tag.TAG_STRING);
    Spell spell = DictionarySavedData.computeIfAbsent(server).parse(LimitedBookSerializer.decodeText(text));
    if(spell != null) {
      if(spell.cost() > level.getGameRules().getInt(ScriptorGameRules.TOME_MAX_COST)) {
        player.sendSystemMessage(Component.translatable("extra.scriptor.fizzle"));
        ScriptorAdvancements.FIZZLE.get().trigger((ServerPlayer) player);
        if(!player.isCreative())
          addCooldown(player, 350);
        return true;
      }
      spell.cast(new SpellbookTargetable(itemStack, player, player.getInventory().selected).withTargetItem(false));
      if(!player.isCreative())
        addCooldown(player, (int) Math.round(spell.cost() * 7));
      return false;
    }
    return true;
  }

  static void addCooldown(Player player, int time) {
    for(var spellbook: SPELLBOOKS)
      player.getCooldowns().addCooldown(spellbook, time);
  }
}
