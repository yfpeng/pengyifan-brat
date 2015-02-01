package com.pengyifan.brat;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Objects;

/**
 * Note annotations provide a way to associate free-form text with either the
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
 * <p>
 * Represented in standoff as
 * 
 * <pre>
 * ID\tTYPE REFID\tNOTE
 */
public class BratNote extends BratAnnotation {

  public static BratNote parseNote(String s) {
    String toks[] = s.split("\\t+");
    checkArgument(toks.length == 3, "Illegal format: %s", s);

    BratNote note = new BratNote();
    note.setId(toks[0]);
    note.setText(toks[2]);

    int index = toks[1].indexOf(' ');
    checkArgument(index != -1, "Illegal format: %s", s);
    note.setType(toks[1].substring(0, index));
    note.setRefId(toks[1].substring(index + 1));

    return note;
  }
  private String refId;

  private String text;

  public BratNote() {
    super();
  }
  
  public BratNote(BratNote note) {
    super(note);
    refId = note.refId;
    text = note.text;
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

  @Override
  public void setId(String id) {
    checkArgument(id.startsWith("#"), "ID should start with #");
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
  
  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), refId, text);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null || obj.getClass() != getClass()) {
      return false;
    }
    BratNote rhs = (BratNote) obj;
    return super.equals(obj)
        && Objects.equals(refId, rhs.refId)
        && Objects.equals(text, rhs.text);
  }
}
