package com.sldpnn.campaign;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class ConsoleSegmentInputProcessor extends AbstractSegmentInputProcessor {

  private static final Logger LOGGER = Logger.getLogger(ConsoleSegmentInputProcessor.class);

  public ConsoleSegmentInputProcessor(final CampaignFinder finder) {
    super(finder, LOGGER);
  }

  @Override
  public void process() {
    LOGGER.info(
        "Please, input segment numbers with whitespace separator like '1 2 3 4':(type 'exit' to break execution)\n");
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
      String line = null;
      while ((line = reader.readLine()) != null) {
        if (StringUtils.equalsIgnoreCase("exit", line)) {
          return;
        }

        processSegmentInputLine(line);
      }
    } catch (final IOException e) {
      LOGGER.error("unable to read user input", e);
    }
  }
}