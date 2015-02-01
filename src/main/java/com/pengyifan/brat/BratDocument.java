package com.pengyifan.brat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class BratDocument {

  private List<BratEvent> events;
  private List<BratRelation> relations;
  private List<BratEntity> entities;
  private List<BratEquivRelation> equivRelations;
  private List<BratAttribute> attributes;
  private List<BratNote> notes;
  private String text;
  private String id;
  private Map<String, BratAnnotation> annotationMap;

  public BratDocument() {
    entities = Lists.newArrayList();
    relations = Lists.newArrayList();
    equivRelations = Lists.newArrayList();
    events = Lists.newArrayList();
    attributes = Lists.newArrayList();
    notes = Lists.newArrayList();
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
    if (ann instanceof BratEntity) {
      getEntities().add((BratEntity) ann);
    } else if (ann instanceof BratEvent) {
      getEvents().add((BratEvent) ann);
    } else if (ann instanceof BratEquivRelation) {
      getEquivRelations().add((BratEquivRelation) ann);
    } else if (ann instanceof BratRelation) {
      getRelations().add((BratRelation) ann);
    } else if (ann instanceof BratAttribute) {
      getAttributes().add((BratAttribute) ann);
    } else if (ann instanceof BratNote) {
      getNotes().add((BratNote) ann);
    } else {
      Validate.isTrue(false, "annotation not instanceof %s", ann);
    }
    Validate.isTrue(
        !annotationMap.containsKey(ann.getId()),
        "already have %s",
        ann.getId());
    annotationMap.put(ann.getId(), ann);
  }

  public Collection<BratAnnotation> getAnnotations() {
    return annotationMap.values();
  }

  public List<BratEvent> getEvents() {
    return events;
  }

  public List<BratEntity> getEntities() {
    return entities;
  }

  public List<BratRelation> getRelations() {
    return relations;
  }

  public List<BratAttribute> getAttributes() {
    return attributes;
  }

  public List<BratEquivRelation> getEquivRelations() {
    return equivRelations;
  }

  public List<BratNote> getNotes() {
    return notes;
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
}
