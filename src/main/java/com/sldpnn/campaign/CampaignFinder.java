package com.sldpnn.campaign;

import static org.apache.commons.collections4.CollectionUtils.intersection;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomUtils;

public class CampaignFinder {

  private final Set<Campaign> campaigns;

  public CampaignFinder(final Set<Campaign> campaigns) {
    this.campaigns = campaigns;
  }

  public String find(final Set<Integer> segments) {
    final Map<String, Integer> segmentCounters = campaigns.parallelStream()
        .collect(Collectors.toMap(c -> c.getName(), c -> intersection(c.getSegmentIds(), segments).size()))
        .entrySet().parallelStream()
        .filter(map -> map.getValue() > 0)
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .collect(Collectors.toMap(Map.Entry::getKey,
            Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

    final int foundCounters = segmentCounters.size();
    if (foundCounters == 0) {
      return "no campaign";
    }

    final String maxCampaign = segmentCounters.keySet().iterator().next();
    if (foundCounters == 1) {
      return maxCampaign;
    }

    final Integer maxValue = segmentCounters.get(maxCampaign);
    // Integer maxValue = Collections.max(segmentCounters.values());

    final List<String> matchedCampaigns = segmentCounters.entrySet().parallelStream()
        .filter(map -> map.getValue() == maxValue)
        .map(map -> map.getKey())
        .collect(Collectors.toList());

    return matchedCampaigns.get(RandomUtils.nextInt(0, matchedCampaigns.size()));
  }
}
