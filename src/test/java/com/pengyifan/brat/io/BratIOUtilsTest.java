package com.pengyifan.brat.io;

import com.pengyifan.brat.BratDocument;
import com.pengyifan.brat.BratEntity;
import com.pengyifan.brat.BratEvent;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class BratIOUtilsTest {

  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  private static final String DOC_ID = "id";
  private BratDocument base;

  @Before
  public void setUp()
      throws IOException, URISyntaxException {
    URL url = this.getClass().getResource("/example1.ann");
    base = BratIOUtils.read(Files.newBufferedReader(Paths.get(url.toURI())), DOC_ID);
  }

  @Test
  public void testReadPath()
      throws IOException, URISyntaxException {
    URL url = this.getClass().getResource("/example1.ann");
    BratDocument actual = BratIOUtils.read(Paths.get(url.toURI()), DOC_ID);
    assertDoc(actual);
  }

  @Test
  public void testReader()
      throws IOException, URISyntaxException {
    URL url = this.getClass().getResource("/example1.ann");
    BratDocument actual = BratIOUtils.read(Files.newBufferedReader(Paths.get(url.toURI())), DOC_ID);
    assertDoc(actual);
  }

  @Test
  public void testWriter()
      throws IOException {
    StringWriter writer = new StringWriter();
    BratIOUtils.write(writer, base);
    BratDocument actual = BratIOUtils.read(new StringReader(writer.toString()), DOC_ID);
    assertDoc(actual);
  }

  @Test
  public void testWritePath()
      throws IOException {
    Path file = temporaryFolder.newFile("output.txt").toPath();
    BratIOUtils.write(Files.newBufferedWriter(file), base);
    BratDocument actual = BratIOUtils.read(Files.newBufferedReader(file), DOC_ID);
    assertDoc(actual);
  }

  private void assertDoc(BratDocument document) {
    assertEquals(DOC_ID, document.getDocId());

    BratEntity entity = document.getEntity("T2");
    assertEquals("Protein", entity.getType());
    assertEquals(161, entity.beginPosition());
    assertEquals(164, entity.endPosition());
    assertEquals("Id1", entity.getText());

    entity = document.getEntity("T7");
    assertEquals("Positive_regulation", entity.getType());
    assertEquals(135, entity.beginPosition());
    assertEquals(146, entity.endPosition());
    assertEquals("consecutive", entity.getText());

    entity = document.getEntity("T8");
    assertEquals("Gene_expression", entity.getType());
    assertEquals(147, entity.beginPosition());
    assertEquals(157, entity.endPosition());
    assertEquals("production", entity.getText());

    BratEvent event = document.getEvent("E1");
    assertEquals("Positive_regulation", event.getType());
    assertEquals("T7", event.getTriggerId());
    assertEquals("E2", event.getArgId("Theme"));

    event = document.getEvent("E2");
    assertEquals("Gene_expression", event.getType());
    assertEquals("T8", event.getTriggerId());
    assertEquals("T2", event.getArgId("Theme"));
  }
}
