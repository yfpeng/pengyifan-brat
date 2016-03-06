package com.pengyifan.brat.io;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

import com.pengyifan.brat.BratDocument;

/**
 * Provides static utility methods for reading and writing with {@link
 * com.pengyifan.brat.BratDocument} instances.
 *
 * @author "Yifan Peng"
 * @since 1.0.0
 */
public class BratIOUtils {

  private BratIOUtils() {
  }

  /**
   * Reads the contents of a brat file into a {@link com.pengyifan.brat.BratDocument}.
   * The file is always closed.
   *
   * @param file the file to read, must not be null
   * @param docId document id
   * @return the brat document
   * @throws IOException in case of an I/O error
   */
  public static BratDocument read(Path file, String docId)
      throws IOException {
    return read(Files.newBufferedReader(file), docId);
  }

  /**
   * Writes the brat document to the specified file. The file is always closed.
   *
   * @param file the file to write to
   * @param doc the brat document to write
   * @throws IOException in case of an I/O error
   */
  public static void write(Path file, BratDocument doc)
      throws IOException {
    write(Files.newBufferedWriter(file), doc);
  }

  /**
   * Reads the contents of a brat file into a {@link com.pengyifan.brat.BratDocument}.
   * The reader is always closed.
   *
   * @param reader the reader to read, must not be null
   * @param docId document id
   * @return the brat document
   * @throws IOException in case of an I/O error
   */
  public static BratDocument read(Reader reader, String docId)
      throws IOException {
    BratDocumentReader r = new BratDocumentReader(reader, docId);
    BratDocument doc = r.read();
    r.close();
    return doc;
  }

  /**
   * Writes the brat document to the specified writer. The writer is always closed.
   *
   * @param writer the writer to write to
   * @param doc the brat document to write
   * @throws IOException in case of an I/O error
   */
  public static void write(Writer writer, BratDocument doc)
      throws IOException {
    BratDocumentWriter w = new BratDocumentWriter(writer);
    w.write(doc);
    w.close();
  }
}
