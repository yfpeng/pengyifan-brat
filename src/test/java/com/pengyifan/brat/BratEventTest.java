package com.pengyifan.brat;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.testing.EqualsTester;

public class BratEventTest {

  private static final String LINE = "E1\tMERGE-ORG:T2 Org1:T1 Org2:T3";

  private static final String ID = "E1";
  private static final String TYPE = "MERGE-ORG";
  private static final String TRIGGER_ID = "T2";
  private static final String ROLE1 = "Org1";
  private static final String ROLE1_ID = "T1";
  private static final String ROLE2 = "Org2";
  private static final String ROLE2_ID = "T3";

  private static final String ID_2 = "E2";
  private static final String TYPE_2 = "MERGE-ORG2";
  private static final String TRIGGER_ID_2 = "T4";
  private static final String ROLE1_ID_2 = "T5";

  private BratEvent base;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() {
    base = new BratEvent();
    base.setId(ID);
    base.setType(TYPE);
    base.setTriggerId(TRIGGER_ID);
    base.putArgument(ROLE1, ROLE1_ID);
    base.putArgument(ROLE2, ROLE2_ID);
  }

  @Test
  public void testEquals() {
    BratEvent baseCopy = new BratEvent(base);

    BratEvent diffId = new BratEvent(base);
    diffId.setId(ID_2);

    BratEvent diffType = new BratEvent(base);
    diffType.setType(TYPE_2);

    BratEvent diffTrigger = new BratEvent(base);
    diffTrigger.setTriggerId(TRIGGER_ID_2);

    BratEvent diffTheme = new BratEvent(base);
    diffTheme.putArgument(ROLE1, ROLE1_ID_2);

    new EqualsTester()
        .addEqualityGroup(base, baseCopy)
        .addEqualityGroup(diffId)
        .addEqualityGroup(diffType)
        .addEqualityGroup(diffTrigger)
        .addEqualityGroup(diffTheme)
        .testEquals();
  }

  @Test
  public void testAllFields() {
    assertEquals(ID, base.getId());
    assertEquals(TYPE, base.getType());
    assertEquals(TRIGGER_ID, base.getTriggerId());
    assertEquals(ROLE1_ID, base.getArgId(ROLE1));
    assertEquals(ROLE2_ID, base.getArgId(ROLE2));
  }

  @Test
  public void testParseEvent() {
    BratEvent entity = BratEvent.parseEvent(LINE);
    assertEquals(ID, entity.getId());
    assertEquals(TYPE, entity.getType());
    assertEquals(TRIGGER_ID, entity.getTriggerId());
    assertEquals(ROLE1_ID, base.getArgId(ROLE1));
    assertEquals(ROLE2_ID, base.getArgId(ROLE2));

    thrown.expect(BratIllegalFormatException.class);
    BratEvent.parseEvent("");
  }

  @Test
  public void testSetId() {
    thrown.expect(NullPointerException.class);
    base.setId(null);

    thrown.expect(IllegalArgumentException.class);
    base.setId("T21");
  }

  @Test
  public void testToBratString() throws Exception {
    assertEquals(LINE, base.toBratString());
  }
}
