package com.pengyifan.brat.util;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.Equator;
import org.apache.commons.lang3.Validate;

import com.pengyifan.brat.BratBaseRelation;
import com.pengyifan.brat.BratDocument;
import com.pengyifan.brat.BratEntity;
import com.pengyifan.brat.BratEquivRelation;
import com.pengyifan.brat.BratEvent;
import com.pengyifan.brat.BratNote;
import com.pengyifan.brat.BratRelation;

/**
 * Merge brat documents into one. Reset IDs for all annotations.
 * 
 * Cannot handle recursive annotations
 */
public class BratMerge {

  private BratDocument newDoc;
  private final BratEntityEquator entityEquator = new BratEntityEquator();

  public BratMerge() {
    newDoc = new BratDocument();
  }

  private BratEntity find(BratEntity oldEntity) {
    for (BratEntity newEntity : newDoc.getEntities()) {
      if (entityEquator.equate(newEntity, oldEntity)) {
        return newEntity;
      }
    }
    return null;
  }

  private BratEvent find(BratEvent oldEvent, BratDocument oldDoc) {
    BratBaseRelationEquator eventEquator = new BratBaseRelationEquator(oldDoc, newDoc);
    for (BratEvent newEvent : newDoc.getEvents()) {
      if (eventEquator.equate(oldEvent, newEvent)) {
        return newEvent;
      }
    }
    return null;
  }

  private BratRelation find(BratRelation oldRel, BratDocument oldDoc) {
    BratBaseRelationEquator relEquator = new BratBaseRelationEquator(oldDoc, newDoc);
    for (BratRelation newRel : newDoc.getRelations()) {
      if (relEquator.equate(oldRel, newRel)) {
        return newRel;
      }
    }
    return null;
  }

  // map: old --> new
  private BratEquivRelation find(BratEquivRelation oldRel, BratDocument oldDoc) {
    BratEquivRelationEquator relEquator = new BratEquivRelationEquator(oldDoc,
        newDoc);
    for (BratEquivRelation newRel : newDoc.getEquivRelations()) {
      if (relEquator.equate(oldRel, newRel)) {
        return newRel;
      }
    }
    return null;
  }

  // map: old --> new
  private BratNote find(BratNote oldNote, Map<String, String> idMap) {
    String newRefId = idMap.get(oldNote.getRefId());
    if (newRefId != null) {
      List<BratNote> newNotes = newDoc.getNotes(newRefId);
      for (BratNote newNote : newNotes) {
        if (newNote.getType().equals(oldNote.getType())
            && newNote.getText().equals(oldNote.getText())) {
          return newNote;
        }
      }
    }
    return null;
  }

  public void addDocument(BratDocument oldDoc) {
    // old: new
    Map<String, String> idMap = new HashMap<String, String>();

    // entity
    for (BratEntity oldEntity : oldDoc.getEntities()) {
      // if contained
      BratEntity newEntity = find(oldEntity);
      if (newEntity == null) {
        newEntity = new BratEntity(oldEntity);
        newEntity.setId("T" + newDoc.getEntities().size());
        newDoc.addAnnotation(newEntity);
      }
      idMap.put(oldEntity.getId(), newEntity.getId());
    }

    // event
    for (BratEvent oldEvent : oldDoc.getEvents()) {
      BratEvent newEvent = find(oldEvent, oldDoc);
      if (newEvent == null) {
        newEvent = new BratEvent();
        newEvent.setId("E" + newDoc.getEvents().size());
        newEvent.setType(oldEvent.getType());

        Validate.isTrue(
            idMap.containsKey(oldEvent.getTriggerId()),
            "dont contain: " + oldEvent.getTriggerId());
        newEvent.setTriggerId(idMap.get(oldEvent.getTriggerId()));
        for (String role : oldEvent.getArguments().keySet()) {
          String argId = oldEvent.getArgId(role);
          checkArgument(
              argId.length() > 0 && argId.charAt(0) == 'T',
              "Does not support recursive matching: %d",
              oldEvent);
          checkArgument(idMap.containsKey(argId), "dont contain: %s", argId);
          newEvent.putArgument(role, idMap.get(argId));
        }
        newDoc.addAnnotation(newEvent);
      }
      idMap.put(oldEvent.getId(), newEvent.getId());
    }

    // relation
    for (BratRelation oldRel : oldDoc.getRelations()) {
      BratRelation newRel = find(oldRel, oldDoc);
      if (newRel == null) {
        newRel = new BratRelation();
        newRel.setId("R" + newDoc.getRelations().size());
        newRel.setType(oldRel.getType());

        for (String role : oldRel.getArguments().keySet()) {
          String argId = oldRel.getArgId(role);
          checkArgument(
              argId.length() > 0 && argId.charAt(0) == 'T',
              "Does not support recursive matching: %d",
              oldRel);
          checkArgument(idMap.containsKey(argId), "dont contain: %s", argId);
          newRel.putArgument(role, idMap.get(argId));
        }
        newDoc.addAnnotation(newRel);
      }
      idMap.put(oldRel.getId(), newRel.getId());
    }

    // attribute
    // for (BratAttribute oldAtt : oldDoc.getAttributes()) {
    // BratAttribute newAtt = find(oldAtt, idMap);
    // if (newAtt == null) {
    // newAtt = new BratAttribute();
    // newAtt.setId("A" + newDoc.getAttributes().size());
    // newAtt.setType(oldAtt.getType());
    // newAtt.setRefId(idMap.get(oldAtt.getRefId()));
    // for (String att : oldAtt.getAttributes()) {
    // newAtt.addAttribute(att);
    // }
    // newDoc.addAnnotation(newAtt);
    // }
    // idMap.put(oldAtt.getId(), newAtt.getId());
    // }

    // equiv
    for (BratEquivRelation oldRel : oldDoc.getEquivRelations()) {
      BratEquivRelation newRel = find(oldRel, oldDoc);
      if (newRel == null) {
        newRel = new BratEquivRelation();
        newRel.setId("*");
        newRel.setType(oldRel.getType());

        for (String argId : oldRel.getArgIds()) {
          checkArgument(idMap.containsKey(argId), "dont contain: %s", argId);
          newRel.addArgId(idMap.get(argId));
        }
        newDoc.addAnnotation(newRel);
      }
      idMap.put(oldRel.getId(), newRel.getId());
    }

    // note
    for (BratNote oldNote : oldDoc.getNotes()) {
      BratNote newNote = find(oldNote, idMap);
      if (newNote == null) {
        newNote = new BratNote();
        newNote.setId("#" + newDoc.getNotes().size());
        newNote.setType(oldNote.getType());

        Validate.isTrue(idMap.containsKey(oldNote.getRefId()), "dont contain: "
            + oldNote.getRefId());
        newNote.setRefId(idMap.get(oldNote.getRefId()));

        newNote.setText(oldNote.getText());
        newDoc.addAnnotation(newNote);
      }
      idMap.put(oldNote.getId(), newNote.getId());
    }
  }

  public BratDocument getDoc() {
    return newDoc;
  }

  class BratEntityEquator implements Equator<BratEntity> {

    @Override
    public boolean equate(BratEntity e1, BratEntity e2) {
      return e1.totalSpan().equals(e2.totalSpan());
    }

    @Override
    public int hash(BratEntity o) {
      return o.hashCode();
    }

  }

  class BratBaseRelationEquator implements Equator<BratBaseRelation> {

    BratDocument doc1;
    BratDocument doc2;

    public BratBaseRelationEquator(
        BratDocument doc1,
        BratDocument doc2) {
      this.doc1 = doc1;
      this.doc2 = doc2;
    }

    @Override
    public boolean equate(BratBaseRelation r1, BratBaseRelation r2) {
      // type
      if (!r1.getType().equals(r2.getType())) {
        return false;
      } else if (!contains(r1, r2, doc1, doc2)) {
        return false;
      } else {
        return contains(r2, r1, doc2, doc1);
      }
    }

    /**
     * for every role:arg in r1, r2 contains role:arg
     */
    private boolean contains(BratBaseRelation r1, BratBaseRelation r2,
        BratDocument doc1, BratDocument doc2) {
      for (String role1 : r1.getArguments().keySet()) {
        String argId1 = r1.getArgId(role1);
        checkArgument(
            argId1.length() > 0 && argId1.charAt(0) == 'T',
            "Does not support recursive matching: %d",
            r1);
        if (!r2.containsRole(role1)) {
          return false;
        }
        String argId2 = r2.getArgId(role1);
        checkArgument(
            argId2.length() > 0 && argId2.charAt(0) == 'T',
            "Does not support recursive matching: %d",
            r2);
        if (!entityEquator.equate(
            (BratEntity) doc1.getAnnotation(argId1),
            (BratEntity) doc2.getAnnotation(argId2))) {
          return false;
        }
      }
      return true;
    }

    @Override
    public int hash(BratBaseRelation arg0) {
      throw new UnsupportedOperationException("hash() is not supported yet.");
    }
  }

  class BratEquivRelationEquator implements Equator<BratEquivRelation> {

    BratDocument doc1;
    BratDocument doc2;

    public BratEquivRelationEquator(
        BratDocument doc1,
        BratDocument doc2) {
      this.doc1 = doc1;
      this.doc2 = doc2;
    }

    @Override
    public boolean equate(BratEquivRelation r1, BratEquivRelation r2) {
      // type
      if (!r1.getType().equals(r2.getType())) {
        return false;
      } else if (!contains(r1, r2)) {
        return false;
      } else {
        return contains(r2, r1);
      }
    }

    /**
     * for every role:arg in r1, r2 contains role:arg
     */
    private boolean contains(BratEquivRelation r1, BratEquivRelation r2) {
      for (String argId1 : r1.getArgIds()) {
        checkArgument(
            argId1.length() > 0 && argId1.charAt(0) == 'T',
            "Does not support recursive matching: %s",
            r1);
        for (String argId2 : r2.getArgIds()) {
          checkArgument(
              argId2.length() > 0 && argId2.charAt(0) == 'T',
              "Does not support recursive matching: %s",
              r2);
          if (entityEquator.equate(
              (BratEntity) doc1.getAnnotation(argId1),
              (BratEntity) doc2.getAnnotation(argId2))) {
            return true;
          }
        }
      }
      return false;
    }

    @Override
    public int hash(BratEquivRelation arg0) {
      throw new UnsupportedOperationException("hash() is not supported yet.");
    }
  }
}
