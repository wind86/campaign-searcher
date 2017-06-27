package com.sldpnn.campaign;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class CampaignRunner {

  private static final Logger LOGGER = Logger.getLogger(CampaignRunner.class);

  private SegmentInputProcessor inputProcessor;

  public CampaignRunner(final Path campaignFilePath, final String inputFileName) {
    final CampaignFinder campaignFinder = new CampaignFinder(loadCampaigns(campaignFilePath));

    if (StringUtils.isBlank(inputFileName)) {
      this.inputProcessor = new ConsoleSegmentInputProcessor(campaignFinder);
    } else {
      this.inputProcessor = new FileSegmentInputProcessor(campaignFinder, inputFileName);
    }
  }

  public void run() {
    final long startTimepoint = System.currentTimeMillis();
    inputProcessor.process();
    LOGGER.info("duration: " + (System.currentTimeMillis() - startTimepoint));
  }

  private Set<Campaign> loadCampaigns(final Path campaignFilePath) {
    LOGGER.info("loading campaigns from file: " + campaignFilePath);

    final Set<Campaign> campaigns = new HashSet<>();
    try (Stream<String> stream = Files.lines(campaignFilePath).parallel()) {
      stream.forEach(campaignLine -> campaigns.add(createCampaign(campaignLine)));
    } catch (final IOException e) {
      LOGGER.error("unable to read campaign file", e);
    }

    LOGGER.info(campaigns.size() + " campaigns loaded");
    return campaigns;
  }

  private Campaign createCampaign(final String campaignLine) {
    final String[] parts = StringUtils.split(campaignLine, StringUtils.SPACE);
    return new Campaign(parts[0],
        IntStream.range(1, parts.length)
          .mapToObj(i -> Integer.valueOf(parts[i]))
          .collect(Collectors.toSet()));
  }

  public static void main(final String[] args) {
    if (ArrayUtils.isEmpty(args)) {
      LOGGER.error("campaign data file is not specified");
      System.exit(1);
    }

    final Path campaignFilePath = Paths.get(args[0]);
    if (!Files.exists(campaignFilePath) || Files.isDirectory(campaignFilePath)) {
      LOGGER.error("campaign data file is not found");
      System.exit(1);
    }

    new CampaignRunner(campaignFilePath, args.length == 2 ? args[1] : null).run();
  }
}
