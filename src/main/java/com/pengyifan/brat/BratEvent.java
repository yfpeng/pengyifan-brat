package com.pengyifan.brat;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents an event annotation. Events are typed annotations that are
 * associated with a specific text expression stating the event (TRIGGER,
 * identifying a TextBoundAnnotation) and have an arbitrary number of
 * arguments, each of which is represented as a ROLE:PARTID pair, where ROLE is
 * a string identifying the role (e.g. "Theme", "Cause") and PARTID the ID of
 * another annotation participating in the event.
 * 
 * <pre>
 * T2 MERGE-ORG 14 27 joint venture
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
 */
public class BratEvent extends BratRelation {

  public BratEvent() {
    super();
  }

  @Override
  public void setId(String id) {
    checkArgument(id.startsWith("E"));
    super.setId(id);
  }

  public void setTriggerId(String id) {
    checkNotNull(getType(), "Event type needs to set first");
    putArgument(getType(), id);
  }

  /**
   * Returns he event triggers, annotations marking the word or words stating
   * each event
   */
  public String getTriggerId() {
    return getArgId(getType());
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(getId())
        .append(getType())
        .append(getTriggerId())
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
    BratEvent rhs = (BratEvent) obj;
    return new EqualsBuilder()
        .append(getId(), rhs.getId())
        .append(getType(), rhs.getType())
        .append(getTriggerId(), rhs.getTriggerId())
        .append(getArguments(), rhs.getArguments())
        .isEquals();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(super.toString());
    sb.append(':').append(getTriggerId());
    for (String role : getArguments().keySet()) {
      sb.append(' ').append(role).append(':').append(getArgId(role));
    }
    return sb.toString();
  }
}