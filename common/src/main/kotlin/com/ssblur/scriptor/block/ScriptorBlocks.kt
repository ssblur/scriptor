package com.ssblur.scriptor.block

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.item.ScriptorTabs
import com.ssblur.unfocused.helper.ColorHelper
import com.ssblur.unfocused.registry.RegistrySupplier
import com.ssblur.unfocused.tab.CreativeTabs.tab
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour

val block_toughness = listOf(0.3f, 1.5f, 5f)

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
  val HIGHLIGHT_MODEL = ScriptorMod.registerBlock("highlight_model") { HighlightBlock(BlockBehaviour.Properties.of()) }

//  Lists of coloured magic blocks, one for each toughness/opacity combination. These lists must be separate to support
//  the DyeColorableBlock 'setColor' method
  val MAGIC_BLOCKS_LISTS: List<List<RegistrySupplier<Block>>> = block_toughness.mapIndexed { index, toughness ->
    ColorHelper.forEachColor {
      val id = it.nameAllLowerCase + "_magic_block_toughness_" + index
      ScriptorMod.registerBlock(id) {
        MagicBlock(color=it.dyeColor, strength=toughness)
      }
    }
  }

  fun register() {
    CASTING_LECTERN.second.tab(ScriptorTabs.SCRIPTOR_TAB)
    WRITING_TABLE.second.tab(ScriptorTabs.SCRIPTOR_TAB)
  }
}
