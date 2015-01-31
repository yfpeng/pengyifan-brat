package com.pengyifan.brat;


/**
 * Note annotations provide a way to associate freeform text with either the
 * document or a specific annotation.
 */
public class BratNote extends BratAnnotation {

  private String refId;
  private String text;

  public BratNote() {
    super();
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
}
