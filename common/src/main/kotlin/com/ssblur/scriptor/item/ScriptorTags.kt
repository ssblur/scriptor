package com.ssblur.scriptor.item

import com.ssblur.scriptor.ScriptorMod
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object ScriptorTags {
  val READABLE_SPELLBOOKS: TagKey<Item> = ScriptorMod.registerItemTag("readable_spellbooks")
  val WRITABLE_SPELLBOOKS: TagKey<Item> = ScriptorMod.registerItemTag("writable_spellbooks")
  val SPELLBOOKS: TagKey<Item> = ScriptorMod.registerItemTag("spellbooks")
}