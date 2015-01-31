package com.pengyifan.brat;

import static com.google.common.base.Preconditions.checkArgument;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;

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
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("id", getId())
        .append("type", getType())
        .append("text", getText())
        .append("spans", getSpans())
        .toString();
  }
}