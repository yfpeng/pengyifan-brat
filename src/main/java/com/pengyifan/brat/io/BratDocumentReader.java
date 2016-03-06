package com.pengyifan.brat.io;

import com.pengyifan.brat.BratAttribute;
import com.pengyifan.brat.BratDocument;
import com.pengyifan.brat.BratEntity;
import com.pengyifan.brat.BratEquivRelation;
import com.pengyifan.brat.BratEvent;
import com.pengyifan.brat.BratNote;
import com.pengyifan.brat.BratRelation;

import java.io.Closeable;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;

/**
 * Reads the brat document from the input stream.
 *
 * @author "Yifan Peng"
 * @since 1.0.0
 */
public class BratDocumentReader implements Closeable {

  private LineNumberReader reader;
  private String docId;
  private String text;

  /**
   * Creates a brat document reader.
   *
   * @param reader a reader
   */
  public BratDocumentReader(Reader reader) {
    this(reader, null, null);
  }

  /**
   * Creates a brat document reader with specified document id.
   *
   * @param reader a reader
   * @param docId  document id
   */
  public BratDocumentReader(Reader reader, String docId) {
    this(reader, docId, null);
  }

  /**
   * Creates a brat document reader with specified document id and text.
   *
   * @param reader a reader
   * @param docId  document id
   * @param text   the text
   */
  public BratDocumentReader(Reader reader, String docId, String text) {
    this.reader = new LineNumberReader(reader);
    this.docId = docId;
    this.text = text;
  }

  /**
   * Reads the brat document.
   *
   * @return the brat document
   * @throws IOException in case of an I/O error
   */
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
      char firstChar = line.charAt(0);
      switch (firstChar) {
      case 'T':
        doc.addAnnotation(BratEntity.parseEntity(line));
        break;
      case 'E':
        doc.addAnnotation(BratEvent.parseEvent(line));
        break;
      case 'R':
        doc.addAnnotation(BratRelation.parseRelation(line));
        break;
      case '#':
        doc.addAnnotation(BratNote.parseNote(line));
        break;
      case 'A':
      case 'M':
        doc.addAnnotation(BratAttribute.parseAttribute(line));
        break;
      case '*':
        doc.addAnnotation(BratEquivRelation.parseEquivRelation(line));
        break;
      default:
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
