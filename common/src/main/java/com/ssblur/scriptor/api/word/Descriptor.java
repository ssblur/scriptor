package com.ssblur.scriptor.api.word;

import com.ssblur.scriptor.api.word.Word;

public abstract class Descriptor extends Word {
  public boolean allowsDuplicates() {
    return false;
  }
}
