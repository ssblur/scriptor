package com.ssblur.scriptor.item;

import com.ssblur.scriptor.ScriptorMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ScriptorTags {
  public static final TagKey<Item> READABLE_SPELLBOOKS = TagKey.create(Registries.ITEM, ScriptorMod.location("readable_spellbooks"));
}
