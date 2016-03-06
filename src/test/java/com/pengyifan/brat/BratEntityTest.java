package com.pengyifan.brat;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import com.google.common.testing.EqualsTester;

public class BratEntityTest {

  private static final String ID = "T1";
  private static final String TYPE = "TYPE";
  private static final String TEXT = "ABC";

  private static final String ID_2 = "T2";
  private static final String TYPE_2 = "TYPE2";
  private static final String TEXT_2 = "DEF";

  private static final Range<Integer> SPAN_1 = Range.closedOpen(48, 53);
  private static final Range<Integer> SPAN_2 = Range.closedOpen(56, 57);
  private static final Range<Integer> SPAN_3 = Range.closedOpen(23, 30);

  private BratEntity base;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() {
    base = new BratEntity();
    base.setId(ID);
    base.addSpan(SPAN_1);
    base.addSpan(SPAN_2.lowerEndpoint(), SPAN_2.upperEndpoint());
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
    
    RangeSet<Integer> actual = TreeRangeSet.create();
    actual.add(SPAN_1);
    actual.add(SPAN_2);
    assertEquals(actual, base.getSpans());
    assertEquals(actual.span(), base.totalSpan());
  }

  @Test
  public void testParseEntity() {
    BratEntity entity = BratEntity.parseEntity("T1\tProtein 48 53\tBMP-6");
    assertEquals("T1", entity.getId());
    assertEquals("Protein", entity.getType());
    assertEquals("BMP-6", entity.getText());
    assertEquals(48, entity.beginPosition());
    assertEquals(53, entity.endPosition());
  }

  @Test
  public void testAddSpan() {
    Range<Integer> span = Range.closed(23, 30);
    thrown.expect(IllegalArgumentException.class);
    base.addSpan(span);

    span = Range.openClosed(23, 30);
    thrown.expect(IllegalArgumentException.class);
    base.addSpan(span);
  }
  
  @Test
  public void testSetId() {
    thrown.expect(NullPointerException.class);
    base.setId(null);

    thrown.expect(IllegalArgumentException.class);
    base.setId("E21");
  }

  @Test
  public void testShift() {
    Range<Integer> range = base.totalSpan();
    assertEquals(48, range.lowerEndpoint().intValue());
    assertEquals(57, range.upperEndpoint().intValue());

    BratEntity entity = BratEntity.shift(base, 2);
    range = entity.totalSpan();
    assertEquals(50, range.lowerEndpoint().intValue());
    assertEquals(59, range.upperEndpoint().intValue());
  }
}
