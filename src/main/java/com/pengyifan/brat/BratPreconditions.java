package com.pengyifan.brat;

import javax.annotation.Nullable;

/**
 * Simple static methods to be called to verify correct Brat formatted string.
 * 
 * @since 1.1.0
 * @author "Yifan Peng"
 */
final class BratPreconditions {

  /**
   * Ensures the truth of an expression involving one or more parameters to the
   * calling method.
   * 
   * @param expression a boolean expression
   * @param format a format string
   * @param args arguments referenced by the format specifiers in the format string.
   */
  public static void checkBratFormatArgument(boolean expression,
      @Nullable String format,
      @Nullable Object... args) {
    if (!expression) {
      throw new BratFormatException(String.format(format, args));
    }
  }
}
