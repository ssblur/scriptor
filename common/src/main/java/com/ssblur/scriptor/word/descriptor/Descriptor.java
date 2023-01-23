package com.ssblur.scriptor.word.descriptor;

import com.ssblur.scriptor.word.Word;

public abstract class Descriptor extends Word {
  public boolean allowsDuplicates() {
    return false;
  }
}
