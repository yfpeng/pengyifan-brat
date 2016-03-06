package com.pengyifan.brat;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static com.pengyifan.brat.BratPreconditions.checkBratFormatArgument;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map.Entry;
import java.util.Objects;

/**
 * Events are typed annotations that are associated with a specific text
 * expression stating the event (TRIGGER, identifying a TextBoundAnnotation)
 * and have an arbitrary number of arguments, each of which is represented as a
 * ROLE:PARTID pair, where ROLE is a string identifying the role (e.g. "Theme",
 * "Cause") and PARTID the ID of another annotation participating in the event.
 * 
 * <pre>
 * T2  MERGE-ORG 14 27 joint venture
 * E1  MERGE-ORG:T2 Org1:T1 Org2:T3
 * </pre>
 * 
 * The event triggers, annotations marking the word or words stating each
 * event, are text-bound annotations and their format is identical to that for
 * entities. (The IDs of triggers occupy the same space as the IDs of entities,
 * and these must not overlap.)
 * <p>
 * As for all annotations, the event ID occurs first, separated by a TAB
 * character. The event trigger is specified as TYPE:ID and identifies the
 * event type and its trigger through the ID. By convention, the event type is
 * specified both in the trigger annotation and the event annotation. The event
 * trigger is separated from the event arguments by SPACE. The event arguments
 * are a SPACE-separated set of ROLE:ID pairs, where ROLE is one of the event-
 * and task-specific argument roles (e.g. Theme, Cause, Site) and the ID
 * identifies the entity or event filling that role. Note that several events
 * can share the same trigger and that while the event trigger should be
 * specified first, the event arguments can appear in any order.
 * <p>
 * Represented in standoff as
 * 
 * <pre>
 *   ID \t TYPE:TRIGGER [ROLE1:PART1 ROLE2:PART2 ...]
 * </pre>
 * 
 * @since 1.0.0
 * @author "Yifan Peng"
 */
public class BratEvent extends BratBaseRelation {

  /**
   * Parses the string argument as an event annotation.
   * 
   * @param s a String containing the event annotation to be parsed
   * @return the event annotation represented by the argument.
   */
  public static BratEvent parseEvent(String s) {

    String toks[] = s.split("\\t+");
    checkBratFormatArgument(toks.length == 2, "Illegal format: %s", s);

    BratEvent event = new BratEvent();
    event.setId(toks[0]);

    toks = toks[1].split(" ");
    int index = toks[0].indexOf(':');
    checkBratFormatArgument(index != -1, "Illegal format: %s", s);

    event.setType(toks[0].substring(0, index));
    event.setTriggerId(toks[0].substring(index + 1));

    for (int i = 1; i < toks.length; i++) {
      index = toks[i].indexOf(':');
      checkBratFormatArgument(index != -1, "Illegal format: %s", s);
      event.putArgument(toks[i].substring(0, index), toks[i].substring(index + 1));
    }

    return event;
  }

  private String triggerId;

  public BratEvent() {
    super();
  }

  public BratEvent(BratEvent event) {
    super(event);
    triggerId = event.triggerId;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof BratEvent)) {
      return false;
    }
    BratEvent rhs = (BratEvent) o;
    return super.equals(o)
        && Objects.equals(triggerId, rhs.triggerId);
  }

  /**
   * Returns he event triggers, annotations marking the word or words stating each event
   */
  public String getTriggerId() {
    return triggerId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), triggerId);
  }

  @Override
  public void setId(String id) {
    checkNotNull(id, "ID should not be null");
    checkArgument(id.length() > 0 && id.charAt(0) == 'E', "ID should start with E");
    super.setId(id);
  }

  public void setTriggerId(String triggerId) {
    this.triggerId = triggerId;
  }

  /**
   * <pre>
   *  ID \t TYPE:TRIGGER [ROLE1:PART1 ROLE2:PART2 ...]
   * </pre>
   * @return
   */
  @Override
  public String toBratString() {
    StringBuilder sb = new StringBuilder(getId());
    // type
    sb.append('\t').append(getType());
    // trigger
    sb.append(':').append(getTriggerId());
    // args
    for(Entry<String, String> entry: getArguments().entrySet()) {
      sb.append(' ').append(entry.getKey()).append(':').append(entry.getValue());
    }
    return sb.toString();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("id", getId())
        .append("type", getType())
        .append("trigger", getTriggerId())
        .append("args", getArguments())
        .toString();
  }
}