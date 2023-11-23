package com.ssblur.scriptor.helpers.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.ssblur.scriptor.data.DictionarySavedData;
import com.ssblur.scriptor.events.reloadlisteners.ArtifactReloadListener;
import com.ssblur.scriptor.loot.ScriptorLoot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.functions.SetContainerLootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class ArtifactItemFunction implements LootItemFunction {
  @Override
  public LootItemFunctionType getType() {
    return ScriptorLoot.ARTIFACT.get();
  }

  @Override
  public ItemStack apply(ItemStack itemStack, LootContext lootContext) {
    var artifact = ArtifactReloadListener.INSTANCE.getRandomArtifact();
    String spell = DictionarySavedData.computeIfAbsent(lootContext.getLevel()).generate(artifact.getSpell());

    var tag = itemStack.getOrCreateTag();
    var scriptor = new CompoundTag();
    scriptor.putString("spell", spell);
    scriptor.putString("title", artifact.getName());
    tag.put("scriptor", scriptor);

    return itemStack;
  }
  public static class ArtifactSerializer
    implements Serializer<ArtifactItemFunction> {
    @Override
    public ArtifactItemFunction deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
      return new ArtifactItemFunction();
    }

    @Override
    public void serialize(JsonObject jsonObject, ArtifactItemFunction object, JsonSerializationContext jsonSerializationContext) {}
  }

}
