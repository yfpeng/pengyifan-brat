package com.pengyifan.brat;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.collect.Maps;

/**
 * Binary relations have a unique ID and are defined by their type (e.g.
 * Origin, Part-of) and their arguments.
 * 
 * <pre>
 * R1 Origin Arg1:T3 Arg2:T4
 * </pre>
 * 
 * The format is similar to that applied for events, with the exception that
 * the annotation does not identify a specific piece of text expressing the
 * relation ("trigger"): the ID is separated by a TAB character, and the
 * relation type and arguments by SPACE.
 * <p>
 * Relation arguments are commonly identified simply as Arg1 and Arg2, but the
 * system can be configured to use any labels (e.g. Anaphor and Antecedent) in
 * the standoff representation.
 */
public class BratRelation extends BratAnnotation {

  /**
   * ROLE:ID
   */
  private Map<String, String> arguments;

  public BratRelation() {
    arguments = Maps.newHashMap();
  }

  @Override
  public void setId(String id) {
    checkArgument(id.startsWith("R"));
    super.setId(id);
  }

  /**
   * 
   * @param role task-specific argument role
   * @param id the entity or event filling that role
   */
  public void putArgument(String role, String id) {
    checkArgument(
        !arguments.containsKey(role),
        "Duplicated role: %s: %s",
        role,
        id);
    arguments.put(role, id);
  }

  /**
   * Returns the entity or event filling that role.
   * 
   * @param role task-specific argument role
   * @return the entity or event filling that role
   */
  public String getArgId(String role) {
    return getArguments().get(role);
  }
  
  public boolean containsRole(String role) {
    return arguments.containsKey(role);
  }

  public Map<String, String> getArguments() {
    return arguments;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(getId())
        .append(getType())
        .append(getArguments())
        .toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != getClass()) {
      return false;
    }
    BratRelation rhs = (BratRelation) obj;
    return new EqualsBuilder()
        .append(getId(), rhs.getId())
        .append(getType(), rhs.getType())
        .append(getArguments(), rhs.getArguments())
        .isEquals();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(super.toString());
    for (String role : arguments.keySet()) {
      sb.append(' ').append(role).append(':').append(arguments.get(role));
    }
    return sb.toString();
  }
}