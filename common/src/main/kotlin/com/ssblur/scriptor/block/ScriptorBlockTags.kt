package com.ssblur.scriptor.block

import com.ssblur.scriptor.ScriptorMod
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

object ScriptorBlockTags {
    @JvmField
    val DO_NOT_PHASE: TagKey<Block> = TagKey.create(Registries.BLOCK, ScriptorMod.location("do_not_phase"))
}
