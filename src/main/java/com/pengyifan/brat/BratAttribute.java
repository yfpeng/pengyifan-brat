package com.pengyifan.brat;

import java.util.ArrayList;
import java.util.List;

public class BratAttribute extends BratAnnotation {

  public BratAttribute() {
    super();
    attributes = new ArrayList<String>();
  }

  private String       refId;
  private List<String> attributes;

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
}
