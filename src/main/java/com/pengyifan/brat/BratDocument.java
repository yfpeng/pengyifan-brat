package com.pengyifan.brat;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Maps;

public class BratDocument {

  private String text;
  private String id;
  private Map<String, BratAnnotation> annotationMap;

  public BratDocument() {
    annotationMap = Maps.newHashMap();
  }

  public BratDocument(BratDocument doc) {
    this();
    setText(doc.getText());
    setDocId(doc.getDocId());
    for (BratAnnotation ann : doc.getAnnotations()) {
      addAnnotation(ann);
    }
  }

  public void setText(String text) {
    this.text = text;
  }

  /**
   * Returns text of the original documents input
   * 
   * @return text of the original documents input
   */
  public String getText() {
    return text;
  }

  public BratAnnotation getAnnotation(String id) {
    Validate.isTrue(annotationMap.containsKey(id), "dont contain %s", id);
    return annotationMap.get(id);
  }

  public BratEntity getEntity(String id) {
    BratAnnotation ann = getAnnotation(id);
    Validate.isInstanceOf(BratEntity.class, ann, "%s is not BratEntity", id);
    return (BratEntity) ann;
  }

  public BratRelation getRelation(String id) {
    BratAnnotation ann = getAnnotation(id);
    Validate
        .isInstanceOf(BratRelation.class, ann, "%s is not BratRelation", id);
    return (BratRelation) ann;
  }

  public BratEvent getEvent(String id) {
    BratAnnotation ann = getAnnotation(id);
    Validate.isInstanceOf(BratEvent.class, ann, "%s is not BratEvent", id);
    return (BratEvent) ann;
  }

  public void addAnnotation(BratAnnotation ann) {
    checkArgument(
        !annotationMap.containsKey(ann.getId()),
        "already have %s",
        ann.getId());
    annotationMap.put(ann.getId(), ann);
  }

  public Collection<BratAnnotation> getAnnotations() {
    return annotationMap.values();
  }

  public Collection<BratEvent> getEvents() {
    return getAnnotations().stream().filter(ann -> ann instanceof BratEvent)
        .map(ann -> (BratEvent) ann).collect(Collectors.toSet());
  }

  public Collection<BratEntity> getEntities() {
    return getAnnotations().stream().filter(ann -> ann instanceof BratEntity)
        .map(ann -> (BratEntity) ann).collect(Collectors.toSet());
  }

  public Collection<BratRelation> getRelations() {
    return getAnnotations().stream().filter(ann -> ann instanceof BratRelation)
        .map(ann -> (BratRelation) ann).collect(Collectors.toSet());
  }

  public Collection<BratAttribute> getAttributes() {
    return getAnnotations().stream()
        .filter(ann -> ann instanceof BratAttribute)
        .map(ann -> (BratAttribute) ann).collect(Collectors.toSet());
  }

  public Collection<BratEquivRelation> getEquivRelations() {
    return getAnnotations().stream()
        .filter(ann -> ann instanceof BratEquivRelation)
        .map(ann -> (BratEquivRelation) ann).collect(Collectors.toSet());
  }

  public Collection<BratNote> getNotes() {
    return getAnnotations().stream().filter(ann -> ann instanceof BratNote)
        .map(ann -> (BratNote) ann).collect(Collectors.toSet());
  }

  /**
   * 
   * @param refId refereed id
   * @return list of BratNote
   */
  public List<BratNote> getNotes(String refId) {
    List<BratNote> notes = new ArrayList<BratNote>();
    for (BratNote note : getNotes()) {
      if (note.getRefId().equals(refId)) {
        notes.add(note);
      }
    }
    return notes;
  }

  public List<BratAttribute> getAttributes(String refId) {
    List<BratAttribute> attributes = new ArrayList<BratAttribute>();
    for (BratAttribute attribute : getAttributes()) {
      if (attribute.getRefId().equals(refId)) {
        attributes.add(attribute);
      }
    }
    return attributes;
  }

  public void setDocId(String id) {
    this.id = id;
  }

  /**
   * Returns document id. Usually document name
   */
  public String getDocId() {
    return id;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("id", getDocId())
        .append("text", getText())
        .append("annotations", getAnnotations())
        .toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        id,
        text,
        annotationMap);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof BratDocument)) {
      return false;
    }
    BratDocument rhs = (BratDocument) o;
    return Objects.equals(id, rhs.id)
        && Objects.equals(text, rhs.text)
        && Objects.equals(annotationMap, rhs.annotationMap);
  }
}
