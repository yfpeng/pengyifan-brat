package com.pengyifan.brat;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Base class for all annotations with an ID.
 * <p>
 * All annotations IDs consist of a single upper-case character identifying the
 * annotation type and a number. The initial ID characters relate to annotation
 * types as follows:
 * 
 * <pre>
 * T: text-bound annotation
 * R: relation
 * E: event
 * A: attribute
 * M: modification (alias for attribute, for backward compatibility)
 * N: normalization
 * #: note
 * </pre>
 */
public abstract class BratAnnotation {

  private String id;
  private String type;

  BratAnnotation() {
  }

  public String getId() {
    checkNotNull(id, "id has to be set");
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    checkNotNull(type, "type has to be set");
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    // id TAB type
    return getId() + "\t" + getType();
  }
}
