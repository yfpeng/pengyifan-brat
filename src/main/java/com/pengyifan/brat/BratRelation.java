package com.pengyifan.brat;

import static com.pengyifan.brat.BratPreconditions.checkBratFormatArgument;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Relations have a unique ID and are defined by their type (e.g. Origin,
 * Part-of) and their arguments.
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
 * <p>
 * Represented in standoff as
 * 
 * <pre>
 * ID\tTYPE [ROLE1:PART1 ROLE2:PART2 ...]
 * </pre>
 * 
 * @since 1.0.0
 * @author "Yifan Peng"
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
      relation.putArgument(
          toks[i].substring(0, index),
          toks[i].substring(index + 1));
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
    checkArgument(
        id.length() > 0 && id.charAt(0) == 'R',
        "ID should start with R");
    super.setId(id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(super.toString());
    getArguments().keySet().stream()
        .sorted()
        .forEach(role -> sb.append(' ').append(role).append(':').append(getArgId(role)));
    return sb.toString();
  }

}