package com.ssblur.scriptor.block

import com.ssblur.scriptor.ScriptorMod
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.state.BlockBehaviour

object ScriptorBlocks {
    val RUNE = ScriptorMod.registerBlock("rune") { RuneBlock() }
    val LIGHT = ScriptorMod.registerBlock("light") { LightBlock() }
    val CHALK = ScriptorMod.registerBlock("chalk") { ChalkBlock() }
    val ENGRAVING = ScriptorMod.registerBlock("engraving") { EngravingBlock() }
    val CASTING_LECTERN = ScriptorMod.registerBlock("casting_lectern") { CastingLecternBlock() }
    val PHASED_BLOCK = ScriptorMod.registerBlock("phased_block") { PhasedBlock() }
    val GENERATE = ScriptorMod.registerBlock("generate") { GenerateBlock() }

    val WHITE_MAGIC_BLOCK = ScriptorMod.registerBlock("white_magic_block") 
        { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE)) }
    val LIGHT_GRAY_MAGIC_BLOCK = ScriptorMod.registerBlock("light_gray_magic_block") 
        { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_GRAY)) }
    val GRAY_MAGIC_BLOCK = ScriptorMod.registerBlock("gray_magic_block") 
        { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.GRAY)) }
    val BLACK_MAGIC_BLOCK = ScriptorMod.registerBlock("black_magic_block") 
        { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.BLACK)) }
    val BROWN_MAGIC_BLOCK = ScriptorMod.registerBlock("brown_magic_block") 
        { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.BROWN)) }
    val RED_MAGIC_BLOCK = ScriptorMod.registerBlock("red_magic_block") 
        { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.RED)) }
    val ORANGE_MAGIC_BLOCK = ScriptorMod.registerBlock("orange_magic_block")
        { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.ORANGE)) }
    val YELLOW_MAGIC_BLOCK = ScriptorMod.registerBlock("yellow_magic_block") 
        { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.YELLOW)) }
    val LIME_MAGIC_BLOCK = ScriptorMod.registerBlock("lime_magic_block") 
        { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.LIME)) }
    val GREEN_MAGIC_BLOCK = ScriptorMod.registerBlock("green_magic_block") 
        { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.GREEN)) }
    val CYAN_MAGIC_BLOCK = ScriptorMod.registerBlock("cyan_magic_block") 
        { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.CYAN)) }
    val LIGHT_BLUE_MAGIC_BLOCK = ScriptorMod.registerBlock("light_blue_magic_block") 
        { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_BLUE)) }
    val BLUE_MAGIC_BLOCK = ScriptorMod.registerBlock("blue_magic_block") 
        { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.BLUE)) }
    val PURPLE_MAGIC_BLOCK = ScriptorMod.registerBlock("purple_magic_block") 
        { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.PURPLE)) }
    val MAGENTA_MAGIC_BLOCK = ScriptorMod.registerBlock("magenta_magic_block") 
        { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.MAGENTA)) }
    val PINK_MAGIC_BLOCK = ScriptorMod.registerBlock("pink_magic_block") 
        { MagicBlock(BlockBehaviour.Properties.of().mapColor(DyeColor.PINK)) }

    fun register() {}
}
