package com.pengyifan.brat.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;

import com.pengyifan.brat.BratAttribute;
import com.pengyifan.brat.BratDocument;
import com.pengyifan.brat.BratEntity;
import com.pengyifan.brat.BratEquivRelation;
import com.pengyifan.brat.BratEvent;
import com.pengyifan.brat.BratNote;
import com.pengyifan.brat.BratRelation;

public class BratAnnotationsReader implements Closeable {

  private LineNumberReader reader;
  private String docId;
  private String text;
  
  public BratAnnotationsReader(Reader reader) {
    this(reader, null, null);
  }

  public BratAnnotationsReader(Reader reader, String docId) {
    this(reader, docId, null);
  }

  public BratAnnotationsReader(Reader reader, String docId, String text) {
    this.reader = new LineNumberReader(reader);
    this.docId = docId;
    this.text = text;
  }

  public BratDocument read()
      throws IOException {
    BratDocument doc = new BratDocument();
    doc.setDocId(docId);
    doc.setText(text);

    String line;
    while ((line = reader.readLine()) != null) {
      if (line.isEmpty()) {
        continue;
      }
      if (line.startsWith("T")) {
        doc.addAnnotation(BratEntity.parseEntity(line));
      } else if (line.startsWith("E")) {
        doc.addAnnotation(BratEvent.parseEvent(line));
      } else if (line.startsWith("R")) {
        doc.addAnnotation(BratRelation.parseRelation(line));
      } else if (line.startsWith("#")) {
        doc.addAnnotation(BratNote.parseNote(line));
      } else if (line.startsWith("A")) {
        doc.addAnnotation(BratAttribute.parseAttribute(line));
      } else if (line.startsWith("M")) {
        doc.addAnnotation(BratAttribute.parseAttribute(line));
      } else if (line.startsWith("*")) {
        doc.addAnnotation(BratEquivRelation.parseEquivRelation(line));
      } else {
        throw new IllegalArgumentException(String.format(
            "cannot parse line: %s",
            line));
      }
    }
    return doc;
  }

  @Override
  public void close()
      throws IOException {
    reader.close();
  }

}
