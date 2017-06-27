package com.sldpnn.campaign;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class Campaign {

  private final String name;
  private final Set<Integer> segmentIds;

  public Campaign(final String name, final Set<Integer> segmentIds) {
    this.name = name;
    this.segmentIds = segmentIds;
  }

  public String getName() {
    return name;
  }

  public Set<Integer> getSegmentIds() {
    return segmentIds;
  }

  @Override
  public String toString() {
    return String.format("campaign: %s, segments: [%s]", name, StringUtils.join(segmentIds, StringUtils.EMPTY));
  }
}
