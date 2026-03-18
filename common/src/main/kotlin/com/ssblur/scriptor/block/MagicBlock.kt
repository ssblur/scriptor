package com.ssblur.scriptor.block

import com.ssblur.unfocused.extension.BlockPropertiesExtension.isEphemeral
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument

class MagicBlock(val color: DyeColor): Block(
  Properties.of().instrument(NoteBlockInstrument.HAT)
    .strength(0.3f)
    .sound(SoundType.GLASS)
    .noOcclusion()
    .mapColor(color)
    .isEphemeral()
)
