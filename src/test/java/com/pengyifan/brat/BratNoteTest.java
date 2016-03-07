package com.pengyifan.brat;

import com.google.common.testing.EqualsTester;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class BratNoteTest {

  private static final String LINE = "#1\tAnnotatorNotes T1\tthis annotation is suspect";

  private static final String ID = "#1";
  private static final String TYPE = "AnnotatorNotes";
  private static final String REFID = "T1";
  private static final String TEXT = "this annotation is suspect";

  private static final String ID_2 = "#2";
  private static final String TYPE_2 = "AnnotatorNotes2";
  private static final String REFID_2 = "T3";
  private static final String TEXT_2 = "this annotation is suspect2";

  private BratNote base;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() {
    base = new BratNote();
    base.setId(ID);
    base.setType(TYPE);
    base.setRefId(REFID);
    base.setText(TEXT);
  }

  @Test
  public void testEquals() {
    BratNote baseCopy = new BratNote(base);

    BratNote diffId = new BratNote(base);
    diffId.setId(ID_2);

    BratNote diffType = new BratNote(base);
    diffType.setType(TYPE_2);

    BratNote diffRefid = new BratNote(base);
    diffRefid.setRefId(REFID_2);

    BratNote diffText = new BratNote(base);
    diffText.setText(TEXT_2);

    new EqualsTester()
        .addEqualityGroup(base, baseCopy)
        .addEqualityGroup(diffId)
        .addEqualityGroup(diffType)
        .addEqualityGroup(diffRefid)
        .addEqualityGroup(diffText)
        .testEquals();
  }

  @Test
  public void testSetId() {
    thrown.expect(NullPointerException.class);
    base.setId(null);

    thrown.expect(IllegalArgumentException.class);
    base.setId("E21");
  }

  @Test
  public void testParseNote() {
    assertEquals(base, BratNote.parseNote(LINE));
  }

  @Test
  public void testToBratString() {
    assertEquals(LINE, base.toBratString());
  }
}