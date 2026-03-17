package com.ssblur.scriptor.resources

import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.data.components.ScriptorDataComponents
import com.ssblur.scriptor.data.saved_data.DictionarySavedData.Companion.computeIfAbsent
import com.ssblur.scriptor.error.WordNotFoundException
import com.ssblur.scriptor.registry.words.WordRegistry.actionRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.descriptorRegistry
import com.ssblur.scriptor.registry.words.WordRegistry.subjectRegistry
import com.ssblur.scriptor.word.PartialSpell
import com.ssblur.scriptor.word.Spell
import com.ssblur.unfocused.data.DataLoaderRegistry.registerSimpleDataLoader
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.Mob
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import kotlin.random.Random

object MobSpellItems {
  class InscribedItemResource {
    class PartialSpellResource(var action: String, var descriptors: List<String>)
    class SpellResource(var subject: String, var spells: List<PartialSpellResource>)

    var name: String = ""
    var spell: SpellResource? = null
    var item: ResourceLocation? = null
    var mob: ResourceLocation? = null
    var chance: Double = 0.0
    var slot: EquipmentSlot = EquipmentSlot.MAINHAND
    var rarity: Rarity? = null

    fun getSpell(): Spell {
      val spells = mutableListOf<PartialSpell>()
      for (spell in spell!!.spells) {
        val action = actionRegistry[spell.action] ?: throw WordNotFoundException(spell.action)
        val descriptors = spell.descriptors.map { descriptorRegistry[it] ?: throw WordNotFoundException(it) }
        spells += PartialSpell(action, *descriptors.toTypedArray())
      }
      return Spell(
        subjectRegistry[spell!!.subject] ?: throw WordNotFoundException(spell!!.subject),
        *spells.toTypedArray()
      )
    }
  }

  val items = ScriptorMod.registerSimpleDataLoader("scriptor/mob_spell_items", InscribedItemResource::class)
  val random = Random(System.nanoTime())

  fun getRandom(): InscribedItemResource = items.values.random()
  fun getRandom(mob: Mob): InscribedItemResource? = items.values.filter {
    it.mob != null
        && BuiltInRegistries.ENTITY_TYPE.containsKey(it.mob!!)
        && mob.type == BuiltInRegistries.ENTITY_TYPE.get(it.mob!!)
        && random.nextDouble() < it.chance
  }.randomOrNull()
  fun giveItem(mob: Mob) {
    getRandom(mob)?.let {
      if(mob.level() is ServerLevel) {
        mob.setGuaranteedDrop(it.slot)
        val item = ItemStack(BuiltInRegistries.ITEM.get(it.item))
        item[DataComponents.ITEM_NAME] = Component.translatable(it.name)
        item[DataComponents.ENCHANTMENT_GLINT_OVERRIDE] = true
        item[DataComponents.RARITY] = it.rarity ?: Rarity.COMMON
        item[ScriptorDataComponents.SPELL] = computeIfAbsent(mob.level() as ServerLevel).generate(it.getSpell())
        mob.setItemSlot(it.slot, item)
      }
    }
  }
}