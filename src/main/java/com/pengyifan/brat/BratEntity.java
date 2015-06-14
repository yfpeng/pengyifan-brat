package com.pengyifan.brat;

import static com.pengyifan.brat.BratPreconditions.checkBratFormatArgument;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import com.google.common.collect.*;

/**
 * Each entity annotation has a unique ID and is defined by type (e.g. Person
 * or Organization) and the span of characters containing the entity mention
 * (represented as a "start end" offset pair). For example,
 * 
 * <pre>
 * T1  Organization 0 4  Sony
 * T3  Organization 33 41  Ericsson
 * T3  Country 75 81 Sweden
 * </pre>
 * 
 * Each line contains one text-bound annotation identifying the entity mention
 * in text.
 * <p>
 * Represented in standoff as
 * 
 * <pre>
 * ID\tTYPE START END\tTEXT
 * </pre>
 * 
 * Where START and END are positive integer offsets identifying the span of the
 * annotation in text and TEXT is the corresponding text. Discontinuous
 * annotations can be represented as
 * 
 * <pre>
 * ID\tTYPE START1 END1;START2 END2;...
 * </pre>
 * 
 * with multiple START END pairs separated by semicolons.
 * 
 * @since 1.0.0
 * @author "Yifan Peng"
 */
public class BratEntity extends BratAnnotation {

  /**
   * Parses the string argument as an entity annotation.
   * 
   * @param s a String containing the entity annotation to be parsed
   * @return the entity annotation represented by the argument.
   */
  public static BratEntity parseEntity(String s) {
    String toks[] = s.split("\t");
    checkBratFormatArgument(toks.length == 3, "Illegal format: %s", s);

    BratEntity entity = new BratEntity();
    entity.setId(toks[0]);
    entity.setText(toks[2]);

    int index = toks[1].indexOf(' ');
    checkBratFormatArgument(index != -1, "Illegal format: %s", s);
    entity.setType(toks[1].substring(0, index));

    for (String loc : toks[1].substring(index + 1).split(";")) {
      int space = loc.indexOf(' ');
      checkBratFormatArgument(space != -1, "Illegal format: %s", s);
      entity.addSpan(
          Integer.parseInt(loc.substring(0, space)),
          Integer.parseInt(loc.substring(space + 1)));
    }
    return entity;
  }

  private RangeSet<Integer> rangeSet;
  private String text;

  public BratEntity() {
    rangeSet = TreeRangeSet.create();
  }

  public BratEntity(BratEntity ent) {
    super(ent);
    text = ent.text;
    rangeSet = TreeRangeSet.create(ent.rangeSet);
  }
  
  /**
   * Create a new entity and shifts the location by "offset
   * @param ent old entity
   * @param offset shifted offset
   * @return new entity
   */
  public static BratEntity shift(BratEntity ent, int offset) {
    BratEntity newEnt = new BratEntity();
    newEnt.setId(ent.getId());
    newEnt.setType(ent.getType());
    newEnt.setText(ent.getText());
    for(Range<Integer> span: ent.getSpans().asRanges()) {
      newEnt.addSpan(offset + span.lowerEndpoint(), offset + span.upperEndpoint());
    }
    return newEnt;
  }

  /**
   * Adds one span of the annotation.
   * 
   * @param start the index of the first character of the annotated span in the
   *          text
   * @param end the index of the first character after the annotated span
   */
  public void addSpan(int start, int end) {
    rangeSet.add(Range.closedOpen(start, end));
  }

  /**
   * Adds one span of the annotation. Range: [start-offset, end-offset).
   * 
   * @param span span of the annotation
   */
  public void addSpan(Range<Integer> span) {
    checkArgument(!span.isEmpty(), "the span is empty: %s", span);
    checkArgument(
        span.lowerBoundType() == BoundType.CLOSED,
        "start-offset has to be closed: %s", span);
    checkArgument(
        span.upperBoundType() == BoundType.OPEN,
        "end-offset has to be closed: %s", span);
    addSpan(span.lowerEndpoint(), span.upperEndpoint());
  }

  /**
   * Return the beginning character offset of the annotation.
   * 
   * @return the beginning character offset of the annotation
   */
  public int beginPosition() {
    return rangeSet.span().lowerEndpoint();
  }

  /**
   * Return the ending character offset of the annotation.
   * 
   * @return the ending character offset of the annotation
   */
  public int endPosition() {
    return rangeSet.span().upperEndpoint();
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof BratEntity)) {
      return false;
    }
    BratEntity rhs = (BratEntity) o;
    return super.equals(o)
        && Objects.equals(text, rhs.text)
        && Objects.equals(rangeSet, rhs.rangeSet);
  }

  /**
   * Returns all discontinuous spans.
   * 
   * @return all discontinuous spans
   */
  public RangeSet<Integer> getSpans() {
    return rangeSet;
  }

  /**
   * Returns the text spanned by the annotation.
   * 
   * @return the text spanned by the annotation
   */
  public String getText() {
    return text;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), text, rangeSet);
  }

  @Override
  public void setId(String id) {
    checkNotNull(id, "ID should not be null");
    checkArgument(
        id.length() > 0 && id.charAt(0) == 'T',
        "ID should start with T");
    super.setId(id);
  }

  /**
   * Sets the text spanned by the annotation.
   * 
   * @param text the text spanned by the annotation
   */
  public void setText(String text) {
    this.text = text;
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

  /**
   * Returns the total span of this annotation.
   * 
   * @return the total span of this annotation
   */
  public Range<Integer> totalSpan() {
    return getSpans().span();
  }
}