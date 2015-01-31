package com.pengyifan.brat;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class BratAnnotation {

  private String id;
  private String type;

  BratAnnotation() {
  }

  public String getId() {
    checkNotNull(id, "id has to be set");
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    checkNotNull(type, "type has to be set");
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
