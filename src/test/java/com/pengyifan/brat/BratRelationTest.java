package com.pengyifan.brat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.testing.EqualsTester;

public class BratRelationTest {

  private static final String ARG = "ARG";
  private static final String THEME = "THEME";

  private static final String ID = "R1";
  private static final String TYPE = "TYPE";
  private static final String ARG_ID = "TRIGGER";
  private static final String THEME_ID = "TEHME";

  private static final String ID_2 = "R2";
  private static final String TYPE_2 = "TYPE2";
  private static final String THEME_ID_2 = "TEHME2";

  private BratRelation base;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() {
    base = new BratRelation();
    base.setId(ID);
    base.setType(TYPE);
    base.putArgument(ARG, ARG_ID);
    base.putArgument(THEME, THEME_ID);
  }

  @Test
  public void test_equals() {
    BratRelation baseCopy = new BratRelation(base);

    BratRelation diffId = new BratRelation(base);
    diffId.setId(ID_2);

    BratRelation diffType = new BratRelation(base);
    diffType.setType(TYPE_2);

    BratRelation diffArg = new BratRelation(base);
    diffArg.putArgument(THEME, THEME_ID_2);

    new EqualsTester()
        .addEqualityGroup(base, baseCopy)
        .addEqualityGroup(diffId)
        .addEqualityGroup(diffType)
        .addEqualityGroup(diffArg)
        .testEquals();
  }

  @Test
  public void test_allFields() {
    assertEquals(ID, base.getId());
    assertEquals(TYPE, base.getType());
    assertTrue(base.containsRole(ARG));
    assertEquals(ARG_ID, base.getArgId(ARG));
    assertEquals(THEME_ID, base.getArgId(THEME));
  }

  @Test
  public void test_parse() {
    BratRelation entity = BratRelation
        .parseRelation("R1\tPositive_regulation Arg1:E1 Theme:E2");
    assertEquals("R1", entity.getId());
    assertEquals("Positive_regulation", entity.getType());
    assertEquals("E1", entity.getArgId("Arg1"));
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
