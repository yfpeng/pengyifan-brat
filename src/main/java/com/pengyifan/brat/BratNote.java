package com.pengyifan.brat;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Note annotations provide a way to associate freeform text with either the
 * document or a specific annotation.
 * 
 * <pre>
 * #1 AnnotatorNotes T1 this annotation is suspect
 * </pre>
 * 
 * Notes with an "ID" starting with # followed by a TAB character attach to
 * specific annotations. For these notes, the second TAB-separated field
 * contains a note type and the ID of the annotation that the note is attached
 * to, and the third TAB-separated field contains the text of the note.
 * <p>
 * The note type can be freely assigned and any number of notes can be attached
 * to a single annotation. (However, currently only a single note of type
 * AnnotatorNotes can be edited from the brat UI.)
 */
public class BratNote extends BratAnnotation {

  private String refId;
  private String text;

  public BratNote() {
    super();
  }

  @Override
  public void setId(String id) {
    checkArgument(id.startsWith("#"));
    super.setId(id);
  }

  /**
   * 
   * @param id the ID of the annotation that the note is attached to
   */
  public void setRefId(String id) {
    this.refId = id;
  }

  /**
   * 
   * @return the ID of the annotation that the note is attached to
   */
  public String getRefId() {
    return refId;
  }

  /**
   * @return the text of the note
   */
  public String getText() {
    return text;
  }

  /**
   * @param text the text of the note
   */
  public void setText(String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(super.toString());
    sb.append(' ').append(getRefId()).append('\t').append(getText());
    return sb.toString();
  }
}
