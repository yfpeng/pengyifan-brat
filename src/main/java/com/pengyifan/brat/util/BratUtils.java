package com.pengyifan.brat.util;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.Range;
import com.pengyifan.brat.BratEntity;
import com.pengyifan.brat.BratRelation;

public final class BratUtils {

  private BratUtils() {
  }

  public static Collection<BratEntity> filtEntities(
      Collection<BratEntity> entities,
      final String type) {
    return entities.stream().filter(entity -> entity.getType().equals(type))
        .collect(Collectors.toList());
  }

  public static Collection<BratRelation> remove(
      Collection<BratRelation> relations, final Collection<String> types) {
    return relations.stream()
        .filter(relation -> types.contains(relation.getType()))
        .collect(Collectors.toList());
  }

  public static Optional<BratEntity> getEnity(Collection<BratEntity> entities,
      int beginIndex, int endIndex) {
    Range<Integer> range = Range.closed(beginIndex, endIndex);
    for (BratEntity entity : entities) {
      if (range.equals(entity.totalSpan())) {
        return Optional.of(entity);
      }
    }
    return Optional.empty();
  }
}
