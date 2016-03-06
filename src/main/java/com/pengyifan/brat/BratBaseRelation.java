package com.pengyifan.brat;

import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;

import com.google.common.collect.Maps;

/**
 * Base class for all relations/events.
 * 
 * @since 1.1.0
 * @author "Yifan Peng"
 */
public abstract class BratBaseRelation extends BratAnnotation {

  /**
   * ROLE:ID
   */
  private SortedMap<String, String> arguments;

  public BratBaseRelation() {
    super();
    arguments = Maps.newTreeMap();
  }

  public BratBaseRelation(BratBaseRelation relation) {
    super(relation);
    arguments = Maps.newTreeMap(relation.arguments);
  }

  /**
   * Returns true if this relation contains an argument for the specified role.
   * 
   * @param role the role whose presence in this relation is to be tested
   * @return true if this relation contains an argument for the specified role
   */
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

  /**
   * Returns all arguments.
   * 
   * @return all arguments
   */
  public Map<String, String> getArguments() {
    return arguments;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), arguments);
  }

  /**
   * Associates the specified id with the specified role in this relation. If
   * the relation previously contained a id for the role, the old id is
   * replaced.
   * 
   * @param role task-specific argument role
   * @param id the entity or event filling that role
   */
  public void putArgument(String role, String id) {
    arguments.put(role, id);
  }
}