package com.pengyifan.brat;

import com.google.common.testing.EqualsTester;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BratAttributeTest {

  private static final String LINE = "A2\tConfidence E2 L1";

  private static final String ID = "A2";
  private static final String TYPE = "Confidence";
  private static final String REFID = "E2";
  private static final String ATT = "L1";

  private static final String ID_2 = "A3";
  private static final String TYPE_2 = "Confidence2";
  private static final String REFID_2 = "E3";
  private static final String ATT_2 = "L12";

  private BratAttribute base;

  @Before
  public void setUp() throws Exception {
    base = new BratAttribute();
    base.setId(ID);
    base.setType(TYPE);
    base.setRefId(REFID);
    base.addAttribute(ATT);
  }

  @Test
  public void testParseAttribute() throws Exception {
    BratAttribute attribute = BratAttribute.parseAttribute(LINE);
    assertEquals(base, attribute);
  }

  @Test
  public void testEquals() throws Exception {
    BratAttribute baseCopy = new BratAttribute(base);

    BratAttribute diffId = new BratAttribute(base);
    diffId.setId(ID_2);

    BratAttribute diffType = new BratAttribute(base);
    diffType.setType(TYPE_2);

    BratAttribute diffRefid = new BratAttribute(base);
    diffRefid.setRefId(REFID_2);

    BratAttribute diffAtt = new BratAttribute(base);
    diffAtt.addAttribute(ATT_2);

    new EqualsTester()
        .addEqualityGroup(base, baseCopy)
        .addEqualityGroup(diffId)
        .addEqualityGroup(diffType)
        .addEqualityGroup(diffRefid)
        .addEqualityGroup(diffAtt)
        .testEquals();
  }

  @Test
  public void testToBratString() throws Exception {
    assertEquals(LINE, base.toBratString());
  }
}