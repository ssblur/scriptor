package com.ssblur.scriptor.helpers.loot;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.ssblur.scriptor.data.DictionarySavedData;
import com.ssblur.scriptor.data.components.ScriptorDataComponents;
import com.ssblur.scriptor.events.reloadlisteners.ArtifactReloadListener;
import com.ssblur.scriptor.item.ScriptorLoot;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class ArtifactItemFunction implements LootItemFunction {
  @Override
  public LootItemFunctionType getType() {
    return ScriptorLoot.ARTIFACT.get();
  }

  @Override
  public ItemStack apply(ItemStack itemStack, LootContext lootContext) {
    var artifact = ArtifactReloadListener.INSTANCE.getRandomArtifact();
    String spell = DictionarySavedData.computeIfAbsent(lootContext.getLevel()).generate(artifact.getSpell());

    itemStack.set(ScriptorDataComponents.SPELL, spell);
    itemStack.set(DataComponents.ITEM_NAME, Component.translatable(artifact.getName()));
    return itemStack;
  }
  public static class ArtifactSerializer implements Codec<ArtifactItemFunction> {
    @Override
    public <T> DataResult<Pair<ArtifactItemFunction, T>> decode(DynamicOps<T> ops, T input) {
      return DataResult.success(null);
    }

    @Override
    public <T> DataResult<T> encode(ArtifactItemFunction input, DynamicOps<T> ops, T prefix) {
      return DataResult.success(null);
    }
  }

}
