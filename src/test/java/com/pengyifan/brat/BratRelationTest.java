package com.pengyifan.brat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.testing.EqualsTester;

public class BratRelationTest {

  private static final String LINE = "R1\tOrigin Arg1:T3 Arg2:T4";

  private static final String ID = "R1";
  private static final String TYPE = "Origin";
  private static final String ROLE1 = "Arg1";
  private static final String ROLE1_ID = "T3";
  private static final String ROLE2 = "Arg2";
  private static final String ROLE2_ID = "T4";

  private static final String ID_2 = "R2";
  private static final String TYPE_2 = "TYPE2";
  private static final String ROLE2_ID_2 = "T5";

  private BratRelation base;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() {
    base = new BratRelation();
    base.setId(ID);
    base.setType(TYPE);
    base.putArgument(ROLE1, ROLE1_ID);
    base.putArgument(ROLE2, ROLE2_ID);
  }

  @Test
  public void testEquals() {
    BratRelation baseCopy = new BratRelation(base);

    BratRelation diffId = new BratRelation(base);
    diffId.setId(ID_2);

    BratRelation diffType = new BratRelation(base);
    diffType.setType(TYPE_2);

    BratRelation diffArg = new BratRelation(base);
    diffArg.putArgument(ROLE2_ID, ROLE2_ID_2);

    new EqualsTester()
        .addEqualityGroup(base, baseCopy)
        .addEqualityGroup(diffId)
        .addEqualityGroup(diffType)
        .addEqualityGroup(diffArg)
        .testEquals();
  }

  @Test
  public void testAllFields() {
    assertEquals(ID, base.getId());
    assertEquals(TYPE, base.getType());
    assertEquals(ROLE1_ID, base.getArgId(ROLE1));
    assertEquals(ROLE2_ID, base.getArgId(ROLE2));
  }

  @Test
  public void testParseRelation() {
    BratRelation relation = BratRelation.parseRelation(LINE);
    assertEquals(ID, relation.getId());
    assertEquals(TYPE, relation.getType());
    assertTrue(relation.containsRole(ROLE1));
    assertEquals(ROLE1_ID, relation.getArgId(ROLE1));
    assertTrue(relation.containsRole(ROLE2));
    assertEquals(ROLE2_ID, relation.getArgId(ROLE2));
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
