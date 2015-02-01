package com.pengyifan.brat;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;

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
 */
public class BratAttribute extends BratAnnotation {

  private String refId;
  private List<String> attributes;

  public BratAttribute() {
    super();
    attributes = new ArrayList<String>();
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

  /**
   * 
   * @return the ID of the annotation that the attribute marks
   */
  public String getRefId() {
    return refId;
  }

  public void addAttribute(String value) {
    getAttributes().add(value);
  }

  public void setAttribute(int i, String value) {
    getAttributes().set(i, value);
  }

  public String getAttribute(int i) {
    return getAttributes().get(i);
  }

  public List<String> getAttributes() {
    return attributes;
  }

  public int numberOfAttributes() {
    return getAttributes().size();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(super.toString());
    sb.append(' ').append(getRefId());
    for (int i = 0; i < numberOfAttributes(); i++) {
      sb.append(' ').append(getAttribute(i));
    }
    return sb.toString();
  }
}
