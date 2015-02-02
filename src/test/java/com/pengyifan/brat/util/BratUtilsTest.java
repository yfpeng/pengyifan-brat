package com.pengyifan.brat.util;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.pengyifan.brat.BratEntity;
import com.pengyifan.brat.util.BratUtils;


public class BratUtilsTest {

  @Test
  public void testRemove() {
    List<BratEntity> list = Lists.newArrayList();
    list.add(createEntity("T1", "Protein"));
    list.add(createEntity("T2", "Protein"));
    list.add(createEntity("T3", "Protein2"));
    
    assertEquals(3, list.size());
    
    Collection<BratEntity> collection = BratUtils.filtEntities(list, "Protein");
    assertEquals(2, collection.size());
  }

  private BratEntity createEntity(final String id, final String type) {
    BratEntity ent = new BratEntity();
    ent.setId(id);
    ent.setType(type);
    return ent;
  }
}
