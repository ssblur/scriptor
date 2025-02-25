package com.ssblur.scriptor.block

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.item.ScriptorTabs
import com.ssblur.unfocused.helper.ColorHelper
import com.ssblur.unfocused.tab.CreativeTabs.tab
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

object ScriptorBlocks {
  val DO_NOT_PHASE: TagKey<Block> = ScriptorMod.registerBlockTag("do_not_phase")

  val RUNE = ScriptorMod.registerBlock("rune") { RuneBlock() }
  val LIGHT = ScriptorMod.registerBlock("light") { LightBlock() }
  val CHALK = ScriptorMod.registerBlock("chalk") { ChalkBlock() }
  val ENGRAVING = ScriptorMod.registerBlock("engraving") { EngravingBlock() }
  val CASTING_LECTERN = ScriptorMod.registerBlockWithItem("casting_lectern") { CastingLecternBlock() }
  val WRITING_TABLE = ScriptorMod.registerBlockWithItem("writing_table") { WritingTableBlock() }
  val PHASED_BLOCK = ScriptorMod.registerBlock("phased_block") { PhasedBlock() }
  val GENERATE = ScriptorMod.registerBlock("generate") { GenerateBlock() }

  val MAGIC_BLOCKS = ColorHelper.forEachColor {
    ScriptorMod.registerBlock(it.nameAllLowerCase + "_magic_block") { MagicBlock(it.dyeColor) }
  }

  fun register() {
    CASTING_LECTERN.second.tab(ScriptorTabs.SCRIPTOR_TAB)
    WRITING_TABLE.second.tab(ScriptorTabs.SCRIPTOR_TAB)
  }
}
