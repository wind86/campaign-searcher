package com.sldpnn.campaign;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public abstract class AbstractSegmentInputProcessor implements SegmentInputProcessor {

  private final CampaignFinder finder;
  private final Logger logger;

  public AbstractSegmentInputProcessor(final CampaignFinder finder, final Logger logger) {
    this.finder = finder;
    this.logger = logger;
  }

  protected void processSegmentInputLine(final String segmentLine) {
    final Set<Integer> segments = Arrays.stream(StringUtils.split(segmentLine, StringUtils.SPACE))
        .parallel()
        .map(segment -> Integer.valueOf(segment))
        .collect(Collectors.toSet());

    final String campaign = finder.find(segments);
    logger.info(String.format("%s : %s", segmentLine, campaign));
  }
}
