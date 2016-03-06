package com.pengyifan.brat;

/**
 * Unchecked exception thrown when a format string contains an illegal syntax that is incompatible
 * with the brat standoff format.
 *
 * @author "Yifan Peng"
 * @since 1.1.0
 */
public class BratIllegalFormatException extends IllegalArgumentException {
  public BratIllegalFormatException(String s) {
    super(s);
  }
}
