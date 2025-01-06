package com.ssblur.scriptor.item

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.ScriptorMod.COMMUNITY_MODE
import com.ssblur.unfocused.tab.CreativeTabs.registerCreativeTab
import com.ssblur.unfocused.tab.CreativeTabs.tab

object ScriptorTabs {
    val SCRIPTOR_TAB = ScriptorMod.registerCreativeTab("scriptor_tab", "itemGroup.scriptor.scriptor_tab") {
        ScriptorItems.TOME_TIER4.get()
    }
    val SCRIPTOR_SPELLBOOKS_TAB = ScriptorMod.registerCreativeTab("scriptor_spellbooks", "itemGroup.scriptor.scriptor_spellbooks") {
        ScriptorItems.SPELLBOOK.get()
    }

    fun register() {
        if (!COMMUNITY_MODE) {
            ScriptorItems.TOME_TIER1.tab(SCRIPTOR_TAB)
            ScriptorItems.TOME_TIER2.tab(SCRIPTOR_TAB)
            ScriptorItems.TOME_TIER3.tab(SCRIPTOR_TAB)
            ScriptorItems.TOME_TIER4.tab(SCRIPTOR_TAB)
            ScriptorItems.SCRAP_TIER1.tab(SCRIPTOR_TAB)
            ScriptorItems.SCRAP_TIER2.tab(SCRIPTOR_TAB)
            ScriptorItems.SCRAP_TIER3.tab(SCRIPTOR_TAB)
        }
    }
}
