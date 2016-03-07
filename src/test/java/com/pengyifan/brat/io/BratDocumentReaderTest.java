package com.pengyifan.brat.io;

import com.pengyifan.brat.BratIllegalFormatException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.StringReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class BratDocumentReaderTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testFile() throws Exception {
    URL url = this.getClass().getResource("/example1.ann");
    BratDocumentReader r = new BratDocumentReader(
        Files.newBufferedReader(Paths.get(url.toURI())), "example1.ann");
    r.read();
    r.close();
  }

  @Test
  public void testEmptyFile() throws Exception {
    BratDocumentReader r = new BratDocumentReader(new StringReader(""));
    r.read();
    r.close();
  }

  @Test
  public void testIllegalFile() throws Exception {
    BratDocumentReader r = new BratDocumentReader(new StringReader("x"));
    thrown.expect(BratIllegalFormatException.class);
    r.read();
    r.close();
  }
}