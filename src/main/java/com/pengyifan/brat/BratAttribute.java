package com.pengyifan.brat;

import static com.pengyifan.brat.BratPreconditions.checkBratFormatArgument;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;
import java.util.Set;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Attribute annotations are binary or multi-valued "flags" that specify
 * further aspects of other annotations. Attributes have a unique ID and are
 * defined by reference to the ID of the annotation that the attribute marks
 * and the attribute value.
 * 
 * <pre>
 * A1  Negation E1
 * A2  Confidence E2 L1
 * </pre>
 * 
 * As for other annotations, the ID is separated by TAB and other fields by
 * space.
 * <p>
 * Binary attributes such as A1 in the above example need only specify the
 * attribute name and the ID of the marked annotation: the value true is
 * implied for the binary attribute. The absence of a binary attribute
 * annotation is interpreted as the attribute having the value false.
 * <p>
 * Multi-valued attributes specify also the attribute value, separated by
 * SPACE. The values of multi-valued attributes are fully configurable.
 * <p>
 * For backward compatibility with existing standoff formats, brat also
 * recognizes the ID prefix "M" for attributes.
 * <p>
 * Represented in standoff as
 * 
 * <pre>
 *   ID \t TYPE REFID [FLAG1 FLAG2 ...]
 * </pre>
 * 
 * @since 1.0.0
 * @author "Yifan Peng"
 */
public class BratAttribute extends BratAnnotation {

  /**
   * Parses the string argument as a attribute annotation.
   * 
   * @param s a String containing the attribute annotation to be parsed
   * @return the attribute annotation represented by the argument.
   */
  public static BratAttribute parseAttribute(String s) {
    String toks[] = s.split("\t");
    checkBratFormatArgument(toks.length == 2, "Illegal format: %s", s);

    BratAttribute att = new BratAttribute();
    att.setId(toks[0]);

    toks = toks[1].split(" ");
    checkBratFormatArgument(toks.length >= 2, "Illegal format: %s", s);

    att.setType(toks[0]);
    att.setRefId(toks[1]);
    for (int i = 2; i < toks.length; i++) {
      att.addAttribute(toks[i]);
    }
    return att;
  }

  private String refId;
  private Set<String> attributes;

  public BratAttribute() {
    super();
    attributes = Sets.newHashSet();
  }

  public BratAttribute(BratAttribute attribute) {
    super(attribute);
    refId = attribute.refId;
    attributes = Sets.newHashSet(attribute.attributes);
  }

  /**
   * Adds "flag" that specifies further aspect of referred annotations.
   * 
   * @param value one flag.
   */
  public void addAttribute(String value) {
    getAttributes().add(value);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof BratAttribute)) {
      return false;
    }
    BratAttribute rhs = (BratAttribute) o;
    return super.equals(o)
        && Objects.equals(refId, rhs.refId)
        && Objects.equals(attributes, rhs.attributes);
  }

  /**
   * Returns all "flags" that specify further aspects of referred annotations.
   * 
   * @return all "flags" that specify further aspects of referred annotations
   */
  public Set<String> getAttributes() {
    return attributes;
  }

  /**
   * Returns the ID of the annotation that the attribute marks.
   * 
   * @return the ID of the annotation that the attribute marks
   */
  public String getRefId() {
    return refId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), refId, attributes);
  }

  @Override
  public void setId(String id) {
    checkNotNull(id, "ID should not be null");
    checkArgument(id.length() > 0 && id.charAt(0) == 'A', "ID should start with A");
    super.setId(id);
  }

  /**
   * <pre>
   *   ID \t TYPE REFID [FLAG1 FLAG2 ...]
   * </pre>
   * @return
   */
  @Override
  public String toBratString() {
    StringBuilder sb = new StringBuilder(getId());
    // type
    sb.append('\t').append(getType());
    // refid
    sb.append(' ').append(getRefId());
    // flags
    for (String attribute : attributes) {
      sb.append(' ').append(attribute);
    }
    return sb.toString();
  }

  /**
   * Sets the ID of the annotation that the attribute marks.
   * 
   * @param id the ID of the annotation that the attribute marks
   */
  public void setRefId(String id) {
    this.refId = id;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("id", getId())
        .append("type", getType())
        .append("refid", getRefId())
        .append("flags", getAttributes())
        .toString();
  }
}
