package com.pengyifan.brat.io;

import static org.junit.Assert.assertEquals;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;

import org.junit.Test;

import com.pengyifan.brat.BratDocument;

public class BratIOUtilsTest {

  private static final String ANN_FILE = "example1.ann";

  @Test
  public void test_success()
      throws IOException {
    URL url = this.getClass().getResource("/" + ANN_FILE);
    BratDocument exptected = BratIOUtils.read(new FileReader(url.getFile()), "x");

    StringWriter writer = new StringWriter();
    BratIOUtils.write(writer, exptected);
    BratDocument actual = BratIOUtils.read(new StringReader(writer.toString()), "x");
    
    assertEquals(exptected, actual);
  }
}
