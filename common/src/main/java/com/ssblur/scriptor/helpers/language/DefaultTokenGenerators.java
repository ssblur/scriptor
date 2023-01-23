package com.ssblur.scriptor.helpers.language;

public class DefaultTokenGenerators {
  public static CharacterClass angConsonants =
    new CharacterClass(3, "b", "c", "d", "f", "g", "h", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "v", "x", "z");
  public static CharacterClass angWeirdConsonants =
    new CharacterClass(1, "w", "y");
  public static CharacterClass angVowels =
    new CharacterClass(3, "a", "e", "i", "o", "u");

  /***
   * A simple generator for some words based on the English
   * alphabet and some of its rules.
   * The first generator, built just to have something functional.
   */
  public static TokenGenerator angGenerator =
    new TokenGenerator(3, 11, false, angVowels, angConsonants, angWeirdConsonants);
  /***
   * A simple generator for articles based on the classes
   * assembled for angGenerator.
   */
  public static TokenGenerator shortAngGenerator =
    new TokenGenerator(1, 3, true, angVowels, angConsonants);
}
