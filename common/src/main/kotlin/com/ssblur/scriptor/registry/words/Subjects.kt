package com.ssblur.scriptor.registry.words

import com.ssblur.scriptor.registry.words.WordRegistry.register
import com.ssblur.scriptor.word.subject.*

@Suppress("unused")
object Subjects {
  val SELF = register("self", SelfSubject())
  val TOUCH = register("touch", TouchSubject())
  val HITSCAN = register("hitscan", HitscanSubject())
  val PROJECTILE = register("projectile", ProjectileSubject())
  val STORM = register("storm", StormSubject())
  val RUNE = register("rune", RuneSubject())
  val ENCHANT = register("enchant", ImbueSubject())
}
