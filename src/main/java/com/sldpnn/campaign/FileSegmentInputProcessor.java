package com.sldpnn.campaign;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

public class FileSegmentInputProcessor extends AbstractSegmentInputProcessor {

  private static final Logger LOGGER = Logger.getLogger(FileSegmentInputProcessor.class);

  private final String segmentsFileName;

  public FileSegmentInputProcessor(final CampaignFinder finder, final String inputFileLocation) {
    super(finder, LOGGER);
    this.segmentsFileName = inputFileLocation;
  }

  @Override
  public void process() {
    final Path segmentsFilePath = Paths.get(segmentsFileName);
    if (!Files.exists(segmentsFilePath) || Files.isDirectory(segmentsFilePath)) {
      LOGGER.error("segments file is not found");
      return;
    }

    try (Stream<String> stream = Files.lines(segmentsFilePath).parallel()) {
      stream.forEach(line -> processSegmentInputLine(line));
    } catch (final IOException e) {
      LOGGER.error("unable to read segments file", e);
    }
  }
}