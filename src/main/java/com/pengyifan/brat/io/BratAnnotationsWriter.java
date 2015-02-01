package com.pengyifan.brat.io;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

import com.pengyifan.brat.BratAnnotation;
import com.pengyifan.brat.BratDocument;

public class BratAnnotationsWriter implements Closeable {

  private BufferedWriter writer;

  public BratAnnotationsWriter(Writer writer) {
    this.writer = new BufferedWriter(writer);
  }

  public void write(BratAnnotation annotation)
      throws IOException {
    writer.write(annotation.toString());
    writer.newLine();
  }

  public <E extends BratAnnotation> void write(Collection<E> annotations)
      throws IOException {
    for (BratAnnotation annotation : annotations) {
      write(annotation);
    }
  }

  public void write(BratDocument doc)
      throws IOException {
    write(doc.getEntities());
    write(doc.getRelations());
    write(doc.getEvents());
    write(doc.getAttributes());
    write(doc.getEquivRelations());
    write(doc.getNotes());
  }

  @Override
  public void close()
      throws IOException {
    writer.flush();
    writer.close();
  }
}
