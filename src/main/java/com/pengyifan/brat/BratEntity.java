package com.pengyifan.brat;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.StringJoiner;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;

/**
 * Each entity annotation has a unique ID and is defined by type (e.g. Person
 * or Organization) and the span of characters containing the entity mention
 * (represented as a "start end" offset pair).
 * 
 * <pre>
 * T1  Organization 0 4  Sony
 * T3  Organization 33 41  Ericsson
 * T3  Country 75 81 Sweden
 * </pre>
 * 
 * Each line contains one text-bound annotation identifying the entity mention
 * in text.
 */
public class BratEntity extends BratAnnotation {

  private RangeSet<Integer> rangeSet;
  private String text;

  public BratEntity() {
    rangeSet = TreeRangeSet.create();
  }

  public BratEntity(BratEntity ent) {
    this();
    setId(ent.getId());
    setType(ent.getType());
    setText(ent.getText());
    getSpans().addAll(ent.getSpans());
  }

  @Override
  public void setId(String id) {
    checkArgument(id.startsWith("T"));
    super.setId(id);
  }

  /**
   * 
   * @return the text spanned by the annotation
   */
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  /**
   * [start-offset, end-offset]
   */
  public void addSpan(Range<Integer> span) {
    checkArgument(
        span.lowerBoundType() == BoundType.CLOSED,
        "start-offset has to be closed");
    checkArgument(
        span.upperBoundType() == BoundType.CLOSED,
        "end-offset has to be closed");
    addSpan(span.lowerEndpoint(), span.upperEndpoint());
  }

  public RangeSet<Integer> getSpans() {
    return rangeSet;
  }

  /**
   * 
   * @param start the index of the first character of the annotated span in the
   *          text
   * @param end the index of the first character after the annotated span
   */
  public void addSpan(int start, int end) {
    rangeSet.add(Range.closed(start, end));
  }

  public Range<Integer> totalSpan() {
    return getSpans().span();
  }

  public int beginPosition() {
    return rangeSet.span().lowerEndpoint();
  }

  public int endPosition() {
    return rangeSet.span().upperEndpoint();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(getId())
        .append(getText())
        .append(getSpans())
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
    BratEntity rhs = (BratEntity) obj;
    return new EqualsBuilder()
        .append(getId(), rhs.getId())
        .append(getText(), rhs.getText())
        .append(getType(), rhs.getType())
        .append(getSpans(), rhs.getSpans())
        .isEquals();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(super.toString());
    // span
    StringJoiner joiner = new StringJoiner(";");
    for (Range<Integer> range : getSpans().asRanges()) {
      joiner.add(range.lowerEndpoint() + " " + range.upperEndpoint());
    }
    sb.append(' ').append(joiner.toString());
    // text
    sb.append('\t').append(getText());
    return sb.toString();
  }
}