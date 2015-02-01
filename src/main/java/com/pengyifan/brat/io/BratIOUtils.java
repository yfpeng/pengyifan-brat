package com.pengyifan.brat.io;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.pengyifan.brat.BratDocument;

public class BratIOUtils {

  private BratIOUtils() {
  }

  public static BratDocument read(Reader reader, String docId)
      throws IOException {
    BratAnnotationsReader r = new BratAnnotationsReader(reader, docId);
    BratDocument doc = r.read();
    r.close();
    return doc;
  }
  
  public static void write(Writer writer, BratDocument doc)
      throws IOException {
    BratAnnotationsWriter w = new BratAnnotationsWriter(writer);
    w.write(doc);
    w.close();
  }
}
