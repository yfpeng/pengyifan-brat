package com.pengyifan.brat.io;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.pengyifan.brat.BratDocument;

public class BratAnnotationsReaderTest {

  private static final String ANN_FILE = "example1.ann";

  @Rule
  public TemporaryFolder testFolder = new TemporaryFolder();

  @Test
  public void test_success()
      throws IOException {
    URL url = this.getClass().getResource("/" + ANN_FILE);
    File expected = new File(url.getFile());

    BratAnnotationsReader reader = new BratAnnotationsReader(new FileReader(
        expected));
    BratDocument doc = reader.read();
    reader.close();

    File actual = testFolder.newFile();
    FileWriter swriter = new FileWriter(actual);
    BratAnnotationsWriter writer = new BratAnnotationsWriter(swriter);
    writer.write(doc);
    writer.close();

    assertEquals(FileUtils.readLines(expected), FileUtils.readLines(actual));
  }
}
