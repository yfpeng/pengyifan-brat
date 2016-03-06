package com.pengyifan.brat.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.pengyifan.brat.BratAttribute;
import com.pengyifan.brat.BratDocument;
import com.pengyifan.brat.BratEntity;
import com.pengyifan.brat.BratEquivRelation;
import com.pengyifan.brat.BratEvent;
import com.pengyifan.brat.BratNote;
import com.pengyifan.brat.BratRelation;

public final class BratUtils {

  private BratUtils() {
  }

  public static Collection<BratEntity> filtEntities(
      Collection<BratEntity> entities,
      final String type) {
    return entities.stream().filter(entity -> entity.getType().equals(type))
        .collect(Collectors.toList());
  }

  public static Collection<BratRelation> remove(
      Collection<BratRelation> relations, final Collection<String> types) {
    return relations.stream()
        .filter(relation -> types.contains(relation.getType()))
        .collect(Collectors.toList());
  }

  public static Optional<BratEntity> getEnity(Collection<BratEntity> entities,
      int beginIndex, int endIndex) {
    Range<Integer> range = Range.closedOpen(beginIndex, endIndex);
    for (BratEntity entity : entities) {
      if (range.equals(entity.totalSpan())) {
        return Optional.of(entity);
      }
    }
    return Optional.empty();
  }

  public static BratDocument reorderEntityByOffset(BratDocument document) {
    BratDocument newDocument = new BratDocument();
    newDocument.setDocId(document.getDocId());
    newDocument.setText(document.getText());

    List<BratEntity> entityList = document.getEntities().stream()
        .sorted((e1, e2) -> Integer.compare(e1.beginPosition(), e2.beginPosition()))
        .collect(Collectors.toList());

    // old, new
    Map<String, String> map = Maps.newHashMap();
    int i = 0;
    for(BratEntity entity: entityList) {
      BratEntity newEntity = new BratEntity(entity);
      newEntity.setId("T" + i++);
      map.put(entity.getId(), newEntity.getId());
      newDocument.addAnnotation(newEntity);
    }
    // event
    for(BratEvent event: document.getEvents()) {
      BratEvent newEvent = new BratEvent(event);
      newEvent.setTriggerId(map.get(event.getTriggerId()));
      for(String key: newEvent.getArguments().keySet()) {
        newEvent.putArgument(key, map.get(event.getArgId(key)));
      }
      newDocument.addAnnotation(newEvent);
    }
    // relation
    for(BratRelation relation: document.getRelations()) {
      BratRelation newRelation = new BratRelation(relation);
      for(String key: newRelation.getArguments().keySet()) {
        newRelation.putArgument(key, map.get(relation.getArgId(key)));
      }
      newDocument.addAnnotation(newRelation);
    }
    // equiv
    for(BratEquivRelation equiv: document.getEquivRelations()) {
      BratEquivRelation newEquiv = new BratEquivRelation();
      for(String id: equiv.getArgIds()) {
        newEquiv.addArgId(map.get(id));
      }
      newDocument.addAnnotation(newEquiv);
    }
    // attribute
    for(BratAttribute attribute: document.getAttributes()) {
      BratAttribute newAttribute = new BratAttribute(attribute);
      newAttribute.setRefId(map.get(attribute.getRefId()));
      newDocument.addAnnotation(newAttribute);
    }
    // note
    for(BratNote note: document.getNotes()) {
      BratNote newNote = new BratNote(note);
      newNote.setRefId(map.get(note.getRefId()));
      newDocument.addAnnotation(newNote);
    }
    return newDocument;
  }
}
