package com.ssblur.scriptor.error;

public class WordNotFoundException extends RuntimeException {
  public WordNotFoundException(String key) {
    super("A word with key \"" + key + "\" was not found.");
  }
}
