package com.pengyifan.brat;

import static com.pengyifan.brat.BratPreconditions.checkBratFormatArgument;
import static com.google.common.base.Preconditions.checkArgument;

import java.util.Objects;
import java.util.Set;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Equivalence ie are symmetric and transitive ie that define
 * sets of annotations to be equivalent in some sense (e.g. referring to the
 * same real-world entity). Such ie can be represented in a compact way
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
 * * \t TYPE ID1 ID2 [...]
 * </pre>
 * 
 * Where "*" is the literal asterisk character.
 * 
 * @since 1.0.0
 * @author "Yifan Peng"
 */
public class BratEquivRelation extends BratAnnotation {

  /**
   * Parses the string argument as a equivalence relation annotation.
   * 
   * @param s a String containing the equivalence relation annotation to be parsed
   * @return the equivalence relation annotation represented by the argument.
   */
  public static BratEquivRelation parseEquivRelation(String s) {
    String toks[] = s.split("\t");
    checkBratFormatArgument(toks.length == 2, "Illegal format: %s", s);

    BratEquivRelation relation = new BratEquivRelation();
    checkBratFormatArgument(toks[0].equals("*"), "Illegal format: %s", s);

    toks = toks[1].split(" ");
    checkBratFormatArgument(toks.length >= 2, "Illegal format: %s", s);
    checkBratFormatArgument(toks[0].equals("Equiv"), "Illegal format: %s", s);

    for (int i = 1; i < toks.length; i++) {
      relation.addArgId(toks[i]);
    }
    return relation;
  }

  private Set<String> argIds;

  public BratEquivRelation() {
    super();
    super.setId("*");
    super.setType("Equiv");
    argIds = Sets.newTreeSet();
  }

  public BratEquivRelation(BratEquivRelation relation) {
    super(relation);
    argIds = Sets.newHashSet(relation.argIds);
  }

  public void addArgId(String argId) {
    checkArgument(!argIds.contains(argId), "Duplicated arg: %s", argId);
    argIds.add(argId);
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

  /**
   * <pre>
   * * \t TYPE ID1 ID2 [...]
   * </pre>
   */
  @Override
  public String toBratString() {
    StringBuilder sb = new StringBuilder(getId());
    // type
    sb.append('\t').append(getType());
    // args
    for(String argid: argIds) {
      sb.append(' ').append(argid);
    }
    return sb.toString();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("id", getId())
        .append("type", getType())
        .append("args", getArgIds())
        .toString();
  }
}
