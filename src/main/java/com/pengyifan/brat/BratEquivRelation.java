package com.pengyifan.brat;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Set;

import com.google.common.collect.Sets;

/**
 * Equivalence relations are symmetric and transitive relations that define
 * sets of annotations to be equivalent in some sense (e.g. referring to the
 * same real-world entity). Such relations can be represented in a compact way
 * as a SPACE-separated list of the IDs of the equivalent annotations.
 * 
 * <pre>
 * T1  Organization 0 43 International Business Machines Corporation
 * T2  Organization 45 48  IBM
 * T3  Organization 52 60  Big Blue
 * *   Equiv T1 T2 T3
 * </pre>
 * 
 * For backward compatibility with existing standoff formats, brat supports
 * also the special "empty" ID value "*" for equivalence relation annotations.
 */
public class BratEquivRelation extends BratAnnotation {

  private Set<String> argIds;

  public BratEquivRelation() {
    super();
    argIds = Sets.newHashSet();
  }

  @Override
  public String getId() {
    return "*";
  }

  @Override
  public void setType(String type) {
    throw new UnsupportedOperationException("Type is always Equiv");
  }

  @Override
  public String getType() {
    return "Equiv";
  }

  @Override
  public void setId(String id) {
    throw new UnsupportedOperationException("Id is always *");
  }

  public void addArgId(String argId) {
    checkArgument(argIds.contains(argId), "Duplicated arg: %s", argId);
  }
  
  public Set<String> getArgIds() {
    return argIds;
  }
  
  public boolean containsArgId(String argId) {
    return argIds.contains(argId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(super.toString());
    for (String argId : argIds) {
      sb.append(' ').append(argId);
    }
    return sb.toString();
  }
}
