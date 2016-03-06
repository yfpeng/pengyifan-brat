package com.pengyifan.brat;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import com.google.common.testing.EqualsTester;
import com.pengyifan.brat.io.BratIOUtils;

public class BratDocumentTest {
  
  private static final String TEXT = "ABC";
  private static final String DOC_ID = "id";
  
  private static final String TEXT_2 = "EFG";
  private static final String DOC_ID_2 = "id2";

  private BratDocument base;

  @Before
  public void setUp()
      throws IOException, URISyntaxException {
    URL url = this.getClass().getResource("/example1.ann");
    base = BratIOUtils.read(Files.newBufferedReader(Paths.get(url.toURI())), DOC_ID);
    base.setText(TEXT);
  }
  
  @Test
  public void testAllFields() {
    assertEquals(DOC_ID, base.getDocId());
    assertEquals(TEXT, base.getText());
  }

  @Test
  public void testEquals() {
    BratDocument baseCopy = new BratDocument(base);

    BratDocument diffId = new BratDocument(base);
    diffId.setDocId(DOC_ID_2);

    BratDocument diffText = new BratDocument(base);
    diffText.setText(TEXT_2);

    new EqualsTester()
        .addEqualityGroup(base, baseCopy)
        .addEqualityGroup(diffId)
        .addEqualityGroup(diffText)
        .testEquals();
  }

  @Test
  public void testGetEntity() {
    BratEntity entity = base.getEntity("T2");
    assertEquals("Protein", entity.getType());
    assertEquals(161, entity.beginPosition());
    assertEquals(164, entity.endPosition());
    assertEquals("Id1", entity.getText());
  }

  @Test
  public void testGetRelation() {
    BratRelation relation = base.getRelation("R1");
    assertEquals("PPI", relation.getType());
    assertEquals("T1", relation.getArgId("Arg1"));
    assertEquals("T2", relation.getArgId("Arg2"));
  }

  @Test
  public void testGetEvent() {
    BratEvent event = base.getEvent("E1");
    assertEquals("Positive_regulation", event.getType());
    assertEquals("T7", event.getTriggerId());
    assertEquals("E2", event.getArgId("Theme"));
  }

  @Test
  public void testGetEquivRelations() {
    BratEquivRelation relation = Iterables.getOnlyElement(base.getEquivRelations());
    assertEquals("*", relation.getId());
    assertEquals("Equiv", relation.getType());
    assertThat(relation.getArgIds(), is(Sets.newHashSet("T1", "T2")));
  }
}
