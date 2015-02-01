package com.pengyifan.brat;

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

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof BratBaseRelation)) {
      return false;
    }
    BratBaseRelation rhs = (BratBaseRelation) o;
    return super.equals(o)
        && Objects.equals(arguments, rhs.arguments);
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

  /**
   * 
   * @param role task-specific argument role
   * @param id the entity or event filling that role
   */
  public void putArgument(String role, String id) {
    arguments.put(role, id);
  }
}