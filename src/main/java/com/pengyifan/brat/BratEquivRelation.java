package com.pengyifan.brat;

import static com.pengyifan.brat.BratPreconditions.checkBratFormatArgument;
import static com.google.common.base.Preconditions.checkArgument;

import java.util.Objects;
import java.util.Set;

import com.google.common.collect.Sets;

/**
 * Equivalence relations are symmetric and transitive relations that define
 * sets of annotations to be equivalent in some sense (e.g. referring to the
 * same real-world entity). Such relations can be represented in a compact way
 * as a SPACE-separated list of the IDs of the equivalent annotations. For
 * example
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
 * <p>
 * Represented in standoff as
 * 
 * <pre>
 * *\tTYPE ID1 ID2 [...]
 * </pre>
 * 
 * Where "*" is the literal asterisk character.
 */
public class BratEquivRelation extends BratAnnotation {

  /**
   * Parses the string argument as a equivalence relation annotation.
   * 
   * @param s a String containing the equivalence relation annotation to be
   *          parsed
   * @return the equivalence relation annotation represented by the argument.
   */
  public static BratEquivRelation parseEquivRelation(String s) {
    String toks[] = s.split("\t");
    checkBratFormatArgument(toks.length == 2, "Illegal format: %s", s);

    BratEquivRelation relation = new BratEquivRelation();
    relation.setId(toks[0]);

    toks = toks[1].split(" ");
    checkBratFormatArgument(toks.length >= 2, "Illegal format: %s", s);

    relation.setType(toks[0]);
    for (int i = 1; i < toks.length; i++) {
      relation.addArgId(toks[i]);
    }
    return relation;
  }

  private Set<String> argIds;

  public BratEquivRelation() {
    super();
    argIds = Sets.newHashSet();
  }

  public BratEquivRelation(BratEquivRelation relation) {
    super(relation);
    argIds = Sets.newHashSet(relation.argIds);
  }

  public void addArgId(String argId) {
    checkArgument(argIds.contains(argId), "Duplicated arg: %s", argId);
  }

  public boolean containsArgId(String argId) {
    return argIds.contains(argId);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof BratEquivRelation)) {
      return false;
    }
    BratEquivRelation rhs = (BratEquivRelation) o;
    return super.equals(o)
        && Objects.equals(argIds, rhs.argIds);
  }

  public Set<String> getArgIds() {
    return argIds;
  }

  @Override
  public String getId() {
    return "*";
  }

  @Override
  public String getType() {
    return "Equiv";
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), argIds);
  }

  @Override
  public void setId(String id) {
    throw new UnsupportedOperationException("Id is always *");
  }

  @Override
  public void setType(String type) {
    throw new UnsupportedOperationException("Type is always Equiv");
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
