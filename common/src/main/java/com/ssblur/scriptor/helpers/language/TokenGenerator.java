package com.ssblur.scriptor.helpers.language;

import java.util.Random;
import java.util.function.Predicate;

public class TokenGenerator {
  protected int minimumLength;
  protected int maximumLength;
  protected boolean noAdjacent;
  protected CharacterClass[] characterClasses;
  protected Predicate<String> validator = (string) -> true;
  private Random random;

  public TokenGenerator(
    int minLength,
    int maxLength,
    boolean noAdjacent,
    CharacterClass... charClasses
    ) {
    minimumLength = minLength;
    maximumLength = maxLength;
    characterClasses = charClasses;
    this.noAdjacent = noAdjacent;
    random = new Random();
  }

  /***
   * Sets a validator which can be used to check tokens
   * Ensure this is able to return true, otherwise this
   * may hang
   * @param predicate The predicate used to validate tokens
   * @return this, for chaining
   */
  public TokenGenerator withCustomValidator(Predicate<String> predicate) {
    validator = predicate;
    return this;
  }

  /***
   * @return A token based on the classes and constraints provided.
   */
  public String generateToken() {
    int length = random.nextInt(maximumLength - minimumLength + 1) + minimumLength;
    int adjacent = 0;
    CharacterClass lastClass = null;
    CharacterClass currentClass = characterClasses[0];
    StringBuilder builder = new StringBuilder();
    while(length > 0) {
      if(characterClasses.length > 1) {
        currentClass = characterClasses[random.nextInt(characterClasses.length)];
        while(currentClass == lastClass && ((adjacent >= currentClass.maximumAdjacent) || noAdjacent))
          currentClass = characterClasses[random.nextInt(characterClasses.length)];
        if(lastClass == currentClass)
          adjacent++;
        else
          adjacent = 1;
        lastClass = currentClass;
      }
      builder.append(currentClass.getCharacter());
      length--;
    }
    return builder.toString();
  }
}
