package com.pengyifan.brat;

import javax.annotation.Nullable;

final class BratPreconditions {

  public static void checkBratFormatArgument(boolean expression,
      @Nullable String format,
      @Nullable Object... args) {
    if (!expression) {
      throw new BratFormatException(String.format(format, args));
    }
  }
}
