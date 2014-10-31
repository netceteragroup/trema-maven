/*
 * Copyright (C) 2014 by Netcetera AG.
 * All rights reserved.
 *
 * The copyright to the computer program(s) herein is the property of Netcetera AG, Switzerland.
 * The program(s) may be used and/or copied only with the written permission of Netcetera AG or
 * in accordance with the terms and conditions stipulated in the agreement/contract under which
 * the program(s) have been supplied.
 */
package com.netcetera.trema.maven;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.maven.plugin.MojoExecutionException;

import static org.junit.Assert.fail;


public class ExportMojoTestUtils {

  static final String TREMA_FILE = "src/test/resources/text.trm";
  static final String BASENAME = "target/classes/test";

  private String extension;

  public ExportMojoTestUtils(String extension) {
    this.extension = extension;
  }


  void executeExportJsonMojo(String[] languages, String[] states) {
    final ExportJsonMojo mojo = new ExportJsonMojo();
    mojo.setBasename(BASENAME);
    executeAbstractExportMojo(languages, states, mojo);
  }

  void executeExportPropertiesMojo(String[] languages, String[] states) {
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
    mojo.setBasename(BASENAME);
    executeAbstractExportMojo(languages, states, mojo);
  }

  private void executeAbstractExportMojo(String[] languages,
      String[] states,
      final AbstractExportMojo mojo) {
    try {
      cleanupExistingPropertiesFiles(languages);

      mojo.setTremaFile(TREMA_FILE);
      mojo.setLanguages(languages);
      mojo.setStates(states);
      mojo.execute();
    } catch (MojoExecutionException e) {
      fail("ExportPropertiesMojo threw exception: " + e.toString());
    }
  }

  private void cleanupExistingPropertiesFiles(String... languages) {
    for (String language : languages) {
      Path path = Paths.get(BASENAME + "_" + language + this.extension);
      if (Files.exists(path)) {
        try {
          Files.delete(path);
        } catch (IOException e) {
          // Ignore
        }
      }
    }
  }

}
