package com.netcetera.trema.maven;

import java.util.Arrays;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.sonatype.plexus.build.incremental.BuildContext;


/**
 * Superclass for trema export mojos. Reads and validates configuration
 * prameters and exports data from a Trema file into supported export type.
 */
public abstract class AbstractExportMojo extends AbstractMojo {

  /**
   * Trema file. Path to the trema file to export.
   *
   * @parameter property="tremaFile"
   * default-value="${project.basedir}/src/main/resources/text.trm"
   */
  private String tremaFile;

  /**
   * Languages. Languages to be exported. If not provided, all languages are
   * exported.
   *
   * @parameter property="languages"
   */
  private String[] languages;

  /**
   * States. The states to be exported. If not provided, all states are
   * exported.
   *
   * @parameter property="states"
   */
  private String[] states;

  /**
   * Inject build context to be used to create file output streams that
   * eclipse/m2eclipse is aware of.
   *
   * @component
   */
  private BuildContext buildContext;


  /**
   * Sets the tremaFile.
   *
   * @param tremaFile the tremaFile to set
   */
  public void setTremaFile(String tremaFile) {
    this.tremaFile = tremaFile;
  }

  /**
   * Sets the languages.
   *
   * @param languages the languages to set
   */
  public void setLanguages(String[] languages) {
    this.languages = languages;
  }

  /**
   * Sets the states.
   *
   * @param states the states to set
   */
  public void setStates(String[] states) {
    this.states = states;
  }


  // used only in testcases
  protected void setBuildContext(BuildContext buildContext) {
    this.buildContext = buildContext;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute() throws MojoExecutionException {

    // debug logging
    String basename = getBasename();
    String defaultlanguage = getDefaultlanguage();
    String[] filters = getFilters();
    Log log = getLog();
    log.debug("Executing " + this.getClass().getSimpleName() + " with parameters: "
        + "tremaFile = " + tremaFile
        + ", basename = " + basename
        + ", languages = " + Arrays.toString(languages)
        + ", defaultlanguage = " + defaultlanguage
        + ", states = " + Arrays.toString(states)
        + ", filters = " + Arrays.toString(filters));

    // validate
    if (tremaFile == null || tremaFile.length() == 0) {
      final String msg = "tremaFile must not be empty";
      log.error(msg);
      throw new MojoExecutionException(msg);
    }
    if (basename == null || basename.length() == 0) {
      final String msg = "basename must not be empty";
      log.error(msg);
      throw new MojoExecutionException(msg);
    }

    // prepare the export configuration
    final TremaExportContext exportContext = new TremaExportContext();
    exportContext.setXmlPathName(tremaFile);
    exportContext.setBaseName(basename);
    exportContext.setType(getExportType());
    if (languages != null && languages.length > 0) {
      exportContext.setLanguages(languages);
    }
    if (defaultlanguage != null && defaultlanguage.length() > 0) {
      exportContext.setDefaultLanguage(defaultlanguage);
    }
    if (states != null && states.length > 0) {
      try {
        exportContext.setStatus(states);
      } catch (IllegalArgumentException e) {
        log.error(e.getMessage());
        throw new MojoExecutionException("Invalid States arguments: " + e.getMessage(), e);
      }
    }

    if (filters != null && filters.length > 0) {
      try {
        exportContext.setFilters(filters);
      } catch (IllegalArgumentException e) {
        log.error(e.getMessage());
        throw new MojoExecutionException("Invalid Filter arguments: " + e.getMessage(), e);
      }
    }

    // export the property files
    final TremaExport exporter = new TremaExport(exportContext, getLog());

    // buildContext not available in test cases
    if (buildContext != null) {
      BuildContextAwareOutputStreamFactory outputStreamFactory = new BuildContextAwareOutputStreamFactory(
          buildContext);
      exporter.setOutputStreamFactory(outputStreamFactory);
    }

    try {
      exporter.execute();
    } catch (final Exception e) {
      throw new MojoExecutionException("Failed to export: " + e.getMessage(), e);
    }
  }

  /**
   * Gets the specific export type for the goal.
   *
   * @return the export type
   */
  protected abstract ExportType getExportType();

  /**
   * Gets the basename.
   *
   * @return the basename
   */
  protected abstract String getBasename();

  /**
   * Gets the defaultlanguage.
   *
   * @return the defaultlanguage
   */
  protected abstract String getDefaultlanguage();

  /**
   * Gets the filters.
   *
   * @return the filters
   */
  protected abstract String[] getFilters();

}
