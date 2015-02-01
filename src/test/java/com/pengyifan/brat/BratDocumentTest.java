package com.pengyifan.brat;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import com.google.common.testing.EqualsTester;
import com.pengyifan.brat.io.BratIOUtils;

public class BratDocumentTest {

  private static final String ANN_FILE = "example1.ann";
  
  private static final String TEXT = "ABC";
  private static final String DOC_ID = "id";
  
  private static final String TEXT_2 = "EFG";
  private static final String DOC_ID_2 = "id2";

  private BratDocument base;

  @Before
  public void setUp()
      throws FileNotFoundException, IOException {
    URL url = this.getClass().getResource("/" + ANN_FILE);
    File expected = new File(url.getFile());
    base = BratIOUtils.read(new FileReader(expected), DOC_ID);
    base.setText(TEXT);
  }
  
  @Test
  public void test_allFields() {
    assertEquals(DOC_ID, base.getDocId());
    assertEquals(TEXT, base.getText());
  }

  @Test
  public void test_equals() {
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
  public void test_getAnnotation() {
    BratAnnotation ann = base.getAnnotation("T1");
    assertEquals("T1", ann.getId());
  }
}
