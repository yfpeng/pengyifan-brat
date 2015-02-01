package com.pengyifan.brat.io;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Files;

import org.junit.Test;

import com.pengyifan.brat.BratDocument;

public class BratIOUtilsTest {

  private static final String ANN_FILE = "example1.ann";

  @Test
  public void test_success()
      throws IOException {
    URL url = this.getClass().getResource("/" + ANN_FILE);
    File expected = new File(url.getFile());

    BratDocument doc = BratIOUtils.read(new FileReader(expected), "x");

    StringWriter writer = new StringWriter();
    BratIOUtils.write(writer, doc);

    assertEquals(
        new String(Files.readAllBytes(expected.toPath())),
        writer.toString());
  }
}
