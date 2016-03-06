package com.pengyifan.brat;

import com.google.common.testing.EqualsTester;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class BratEquivRelationTest {

  private static final String LINE = "*\tEquiv T1 T2 T3";
  private static final String ARG1 = "T1";
  private static final String ARG2 = "T2";
  private static final String ARG3 = "T3";
  private static final String ARG4 = "T4";

  private BratEquivRelation base;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() {
    base = new BratEquivRelation();
    base.addArgId(ARG1);
    base.addArgId(ARG2);
    base.addArgId(ARG3);
  }

  @Test
  public void testEquals() {
    BratEquivRelation baseCopy = new BratEquivRelation(base);

    BratEquivRelation diffArg = new BratEquivRelation(base);
    diffArg.addArgId(ARG4);

    new EqualsTester()
        .addEqualityGroup(base, baseCopy)
        .addEqualityGroup(diffArg)
        .testEquals();
  }

  @Test
  public void testAllFields() {
    assertEquals("*", base.getId());
    assertEquals("Equiv", base.getType());
    assertTrue(base.containsArgId(ARG1));
    assertTrue(base.containsArgId(ARG2));
    assertTrue(base.containsArgId(ARG3));
  }

  @Test
  public void testParseEquivRelation() throws Exception {
    BratEquivRelation relation = BratEquivRelation.parseEquivRelation(LINE);
    assertEquals("*", relation.getId());
    assertEquals("Equiv", relation.getType());
    assertTrue(relation.containsArgId(ARG1));
    assertTrue(relation.containsArgId(ARG2));
    assertTrue(relation.containsArgId(ARG3));
  }

  @Test
  public void testSetId() throws Exception {
    thrown.expect(UnsupportedOperationException.class);
    base.setId(null);

    thrown.expect(UnsupportedOperationException.class);
    base.setId("T21");
  }

  @Test
  public void testSetType() throws Exception {
    thrown.expect(UnsupportedOperationException.class);
    base.setType(null);

    thrown.expect(UnsupportedOperationException.class);
    base.setType("type");
  }

  @Test
  public void testToBratString() throws Exception {
    assertEquals(LINE, base.toBratString());
  }
}