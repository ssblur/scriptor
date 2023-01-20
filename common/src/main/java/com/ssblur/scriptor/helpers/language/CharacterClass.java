package com.ssblur.scriptor.helpers.language;

import java.util.Random;

public class CharacterClass {
  String[] characters;
  int maximumAdjacent = 2;
  private Random random;

  /***
   * A character class.
   * Prefer Strings rather than chars because some multiple-char
   * combinations may be atomic within a character class.
   * @param maxAdjacent The highest number of characters within this
   *                    class that may be adjacent to one another.
   * @param chars All characters within this class.
   */
  public CharacterClass(int maxAdjacent, String... chars) {
    characters = chars;
    maximumAdjacent = maxAdjacent;
    random = new Random();
  }

  public String getCharacter() {
    return characters[random.nextInt(characters.length)];
  }
}
