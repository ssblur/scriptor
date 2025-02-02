package com.ssblur.scriptor.api.word

abstract class Descriptor: Word() {
  open fun allowsDuplicates(): Boolean {
    return false
  }
}
