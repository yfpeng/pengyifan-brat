package com.pengyifan.brat;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Map.Entry;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.pengyifan.brat.BratPreconditions.checkBratFormatArgument;

/**
 * Relations have a unique ID and are defined by their type (e.g. Origin, Part-of) and their
 * arguments.
 * <p>
 * <pre>
 * R1 Origin Arg1:T3 Arg2:T4
 * </pre>
 * <p>
 * The format is similar to that applied for events, with the exception that
 * the annotation does not identify a specific piece of text expressing the
 * relation ("trigger"): the ID is separated by a TAB character, and the
 * relation type and arguments by SPACE.
 * <p>
 * Relation arguments are commonly identified simply as Arg1 and Arg2, but the
 * system can be configured to use any labels (e.g. Anaphor and Antecedent) in
 * the standoff representation.
 * <p>
 * Represented in standoff as
 * <p>
 * <pre>
 * ID \t TYPE [ROLE1:PART1 ROLE2:PART2 ...]
 * </pre>
 *
 * @author "Yifan Peng"
 * @since 1.0.0
 */
public class BratRelation extends BratBaseRelation {

  /**
   * Parses the string argument as a relation annotation.
   *
   * @param s a String containing the relation annotation to be parsed
   * @return the relation annotation represented by the argument.
   */
  public static BratRelation parseRelation(String s) {
    String toks[] = s.split("\\t+");
    checkBratFormatArgument(toks.length == 2, "Illegal format: %s", s);

    BratRelation relation = new BratRelation();
    relation.setId(toks[0]);

    toks = toks[1].split(" ");
    relation.setType(toks[0]);

    for (int i = 1; i < toks.length; i++) {
      int index = toks[i].indexOf(':');
      checkBratFormatArgument(index != -1, "Illegal format: %s", s);
      relation.putArgument(toks[i].substring(0, index), toks[i].substring(index + 1));
    }

    return relation;
  }

  public BratRelation() {
  }

  public BratRelation(BratRelation relation) {
    super(relation);
  }

  @Override
  public void setId(String id) {
    checkNotNull(id, "ID should not be null");
    checkArgument(id.length() > 0 && id.charAt(0) == 'R', "ID should start with R");
    super.setId(id);
  }

  /**
   * <pre>
   * ID \t TYPE [ROLE1:PART1 ROLE2:PART2 ...]
   * </pre>
   */
  @Override
  public String toBratString() {
    StringBuilder sb = new StringBuilder(getId());
    // type
    sb.append('\t').append(getType());
    // args
    for (Entry<String, String> entry : getArguments().entrySet()) {
      sb.append(' ').append(entry.getKey()).append(':').append(entry.getValue());
    }
    return sb.toString();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("id", getId())
        .append("type", getType())
        .append("args", getArguments())
        .toString();
  }

}