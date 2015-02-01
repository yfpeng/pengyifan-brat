package com.pengyifan.brat;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.testing.EqualsTester;

public class BratEventTest {

  private static final String THEME = "THEME";

  private static final String ID = "E1";
  private static final String TYPE = "TYPE";
  private static final String TRIGGER = "TRIGGER";
  private static final String THEME_ID = "TEHME";

  private static final String ID_2 = "E2";
  private static final String TYPE_2 = "TYPE2";
  private static final String TRIGGER_2 = "TRIGGER2";
  private static final String THEME_ID_2 = "TEHME2";

  private BratEvent base;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() {
    base = new BratEvent();
    base.setId(ID);
    base.setType(TYPE);
    base.setTriggerId(TRIGGER);
    base.putArgument(THEME, THEME_ID);
  }

  @Test
  public void test_equals() {
    BratEvent baseCopy = new BratEvent(base);

    BratEvent diffId = new BratEvent(base);
    diffId.setId(ID_2);

    BratEvent diffType = new BratEvent(base);
    diffType.setType(TYPE_2);

    BratEvent diffTrigger = new BratEvent(base);
    diffTrigger.setTriggerId(TRIGGER_2);

    BratEvent diffTheme = new BratEvent(base);
    diffTheme.putArgument(THEME, THEME_ID_2);

    new EqualsTester()
        .addEqualityGroup(base, baseCopy)
        .addEqualityGroup(diffId)
        .addEqualityGroup(diffType)
        .addEqualityGroup(diffTrigger)
        .addEqualityGroup(diffTheme)
        .testEquals();
  }

  @Test
  public void test_allFields() {
    assertEquals(ID, base.getId());
    assertEquals(TYPE, base.getType());
    assertEquals(TRIGGER, base.getTriggerId());
    assertEquals(THEME_ID, base.getArgId(THEME));
  }

  @Test
  public void test_parse() {
    BratEvent entity = BratEvent
        .parseEvent("E1\tPositive_regulation:T7 Theme:E2");
    assertEquals("E1", entity.getId());
    assertEquals("Positive_regulation", entity.getType());
    assertEquals("T7", entity.getTriggerId());
    assertEquals("E2", entity.getArgId("Theme"));
  }

  @Test
  public void test_setId() {
    thrown.expect(NullPointerException.class);
    base.setId(null);
  }

  @Test
  public void test_setId2() {
    thrown.expect(IllegalArgumentException.class);
    base.setId("T21");
  }

}
