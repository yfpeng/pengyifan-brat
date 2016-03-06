package com.pengyifan.brat;

import javax.annotation.Nullable;

/**
 * Simple static methods to be called to verify correct Brat formatted string.
 *
 * @author "Yifan Peng"
 * @since 1.1.0
 */
final class BratPreconditions {

  /**
   * Ensures the truth of an expression involving one or more parameters to the
   * calling method.
   *
   * @param expression a boolean expression
   * @param format     a format string
   * @param args       arguments referenced by the format specifiers in the format string.
   * @throws BratIllegalFormatException when a format string contains an illegal syntax that is
   *                             incompatible with the brat format
   */
  public static void checkBratFormatArgument(boolean expression,
      @Nullable String format,
      @Nullable Object... args) {
    if (!expression) {
      throw new BratIllegalFormatException(String.format(format, args));
    }
  }
}
