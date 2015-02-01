package com.pengyifan.brat;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

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
  
  BratAnnotation(BratAnnotation annotation) {
    this.id = annotation.id;
    this.type = annotation.type;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof BratAnnotation)) {
      return false;
    }
    BratAnnotation rhs = (BratAnnotation) obj;
    return Objects.equals(id, rhs.id)
        && Objects.equals(type, rhs.type);
  }

  public String getId() {
    checkNotNull(id, "id has to be set");
    return id;
  }

  public String getType() {
    checkNotNull(type, "type has to be set");
    return type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, type);
  }

  public void setId(String id) {
    this.id = id;
  }
  
  public void setType(String type) {
    this.type = type;
  }
  
  @Override
  public String toString() {
    return getId() + "\t" + getType();
  }
}
