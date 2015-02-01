package com.pengyifan.brat;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.Range;
import com.google.common.testing.EqualsTester;

public class BratEntityTest {

  private final static String LINE = "T1\tProtein 48 53\tBMP-6";
  
  private static final String ID = "T1";
  private static final String TYPE = "TYPE";
  private static final String TEXT = "ABC";

  private static final String ID_2 = "T2";
  private static final String TYPE_2 = "TYPE2";
  private static final String TEXT_2 = "DEF";

  private static final Range<Integer> SPAN_1 = Range.closed(48, 53);
  private static final Range<Integer> SPAN_2 = Range.closed(56, 57);
  private static final Range<Integer> SPAN_3 = Range.closed(23, 30);

  private BratEntity base;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() {
    base = new BratEntity();
    base.setId(ID);
    base.addSpan(SPAN_1);
    base.addSpan(SPAN_2);
    base.setText(TEXT);
    base.setType(TYPE);
  }

  @Test
  public void test_equals() {
    BratEntity baseCopy = new BratEntity(base);

    BratEntity diffId = new BratEntity(base);
    diffId.setId(ID_2);

    BratEntity diffType = new BratEntity(base);
    diffType.setType(TYPE_2);

    BratEntity diffSpan = new BratEntity(base);
    diffSpan.addSpan(SPAN_3);

    BratEntity diffText = new BratEntity(base);
    diffText.setText(TEXT_2);

    new EqualsTester()
        .addEqualityGroup(base, baseCopy)
        .addEqualityGroup(diffId)
        .addEqualityGroup(diffType)
        .addEqualityGroup(diffSpan)
        .addEqualityGroup(diffText)
        .testEquals();
  }

  @Test
  public void test_allFields() {
    assertEquals(ID, base.getId());
    assertEquals(TEXT, base.getText());
    assertEquals(TYPE, base.getType());
    assertEquals(SPAN_1.lowerEndpoint().intValue(), base.beginPosition());
    assertEquals(SPAN_2.upperEndpoint().intValue(), base.endPosition());
  }

  @Test
  public void test_parse() {
    BratEntity entity = BratEntity.parseEntity(LINE);
    assertEquals("T1", entity.getId());
    assertEquals("Protein", entity.getType());
    assertEquals("BMP-6", entity.getText());
    assertEquals(48, entity.beginPosition());
    assertEquals(53, entity.endPosition());
  }

}
