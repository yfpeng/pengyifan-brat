package com.pengyifan.brat;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static com.pengyifan.brat.BratPreconditions.checkBratFormatArgument;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

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
 * </pre>
 * 
 * @since 1.0.0
 * @author "Yifan Peng"
 */
public class BratNote extends BratAnnotation {

  /**
   * Parses the string argument as a note annotation.
   * 
   * @param s a String containing the note annotation to be parsed
   * @return the note annotation represented by the argument.
   */
  public static BratNote parseNote(String s) {
    String toks[] = s.split("\\t+");
    checkBratFormatArgument(toks.length == 3, "Illegal format: %s", s);

    BratNote note = new BratNote();
    note.setId(toks[0]);
    note.setText(toks[2]);

    int index = toks[1].indexOf(' ');
    checkBratFormatArgument(index != -1, "Illegal format: %s", s);
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

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof BratNote)) {
      return false;
    }
    BratNote rhs = (BratNote) o;
    return super.equals(o)
        && Objects.equals(refId, rhs.refId)
        && Objects.equals(text, rhs.text);
  }

  /**
   * Returns the ID of the annotation that the note is attached to.
   * 
   * @return the ID of the annotation that the note is attached to
   */
  public String getRefId() {
    return refId;
  }

  /**
   * Returns the text of the note.
   * 
   * @return the text of the note
   */
  public String getText() {
    return text;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), refId, text);
  }

  @Override
  public void setId(String id) {
    checkNotNull(id, "ID should not be null");
    checkArgument(
        id.length() > 0 && id.charAt(0) == '#',
        "ID should start with #");
    super.setId(id);
  }

  /**
   * Sets the ID of the annotation that the note is attached to.
   * 
   * @param id the ID of the annotation that the note is attached to
   */
  public void setRefId(String id) {
    this.refId = id;
  }

  /**
   * Sets the text of the note.
   * 
   * @param text the text of the note
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * <pre>
   *   ID \t TYPE REFID \t NOTE
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
    // note
    sb.append('\t').append(getText());
    return sb.toString();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("id", getId())
        .append("type", getType())
        .append("refid", getRefId())
        .append("note", getText())
        .toString();
  }
}
