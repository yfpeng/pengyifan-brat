package com.pengyifan.brat;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Map;
import java.util.Objects;

import com.google.common.collect.Maps;

/**
 * Base class for all relations/events.
 */
public abstract class BratBaseRelation extends BratAnnotation {

  /**
   * ROLE:ID
   */
  private Map<String, String> arguments;

  public BratBaseRelation() {
    super();
    arguments = Maps.newHashMap();
  }
  
  public BratBaseRelation(BratBaseRelation relation) {
    super(relation);
    arguments = Maps.newHashMap(relation.arguments);
  }

  public boolean containsRole(String role) {
    return arguments.containsKey(role);
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

  public Map<String, String> getArguments() {
    return arguments;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), arguments);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != getClass()) {
      return false;
    }
    BratBaseRelation rhs = (BratBaseRelation) obj;
    return super.equals(obj)
        && Objects.equals(arguments, rhs.arguments);
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

}