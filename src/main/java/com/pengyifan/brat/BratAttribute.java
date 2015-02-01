package com.pengyifan.brat;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Objects;
import java.util.Set;

import com.google.common.collect.Sets;

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
 * ID\tTYPE REFID [FLAG1 FLAG2 ...]
 */
public class BratAttribute extends BratAnnotation {

  public static BratAttribute parseAttribute(String s) {
    String toks[] = s.split("\t");
    checkArgument(toks.length == 2, "Illegal format: %s", s);

    BratAttribute att = new BratAttribute();
    att.setId(toks[0]);

    toks = toks[1].split(" ");
    checkArgument(toks.length >= 2, "Illegal format: %s", s);

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

  public void addAttribute(String value) {
    getAttributes().add(value);
  }

  public Set<String> getAttributes() {
    return attributes;
  }

  /**
   * 
   * @return the ID of the annotation that the attribute marks
   */
  public String getRefId() {
    return refId;
  }

  public int numberOfAttributes() {
    return getAttributes().size();
  }

  @Override
  public void setId(String id) {
    checkArgument(id.startsWith("A"));
    super.setId(id);
  }

  /**
   * 
   * @param id the ID of the annotation that the attribute marks
   */
  public void setRefId(String id) {
    this.refId = id;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(super.toString());
    sb.append(' ').append(getRefId());
    for (String attribute: attributes) {
      sb.append(' ').append(attribute);
    }
    return sb.toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), refId, attributes);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != getClass()) {
      return false;
    }
    BratAttribute rhs = (BratAttribute) obj;
    return super.equals(obj)
        && Objects.equals(refId, rhs.refId)
        && Objects.equals(attributes, rhs.attributes);
  }
}
