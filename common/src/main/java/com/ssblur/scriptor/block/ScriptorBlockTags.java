package com.ssblur.scriptor.block;

import com.ssblur.scriptor.ScriptorMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ScriptorBlockTags {
  public static final TagKey<Block> DO_NOT_PHASE = TagKey.create(Registries.BLOCK, ScriptorMod.location("do_not_phase"));
}
