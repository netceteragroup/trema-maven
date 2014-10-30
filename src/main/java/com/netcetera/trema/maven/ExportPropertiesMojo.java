package com.netcetera.trema.maven;


/**
 * Export data from a Trema file into property files.
 *
 * @goal exportProperties
 * @phase process-sources
 * @threadSafe
 */
public class ExportPropertiesMojo extends AbstractExportMojo {

  /**
   * Basename for the export. Path and beginning of the filename to be used for
   * the properties.
   *
   * @parameter property="basename"
   * default-value="${project.build.directory}/classes/text"
   */
  private String basename;


  /**
   * Defaultlanguage. If specified, this language will be exported into a
   * default properties file without the language in the filename, eg.
   * text.properties.
   *
   * @parameter property="defaultlanguage"
   */
  private String defaultlanguage;

  /**
   * Export filters. Filters to be applied for transformation of the text values
   * during properties file exporting. Possible values are: 'messageformat'
   * (converts one singlequote into two singlequotes) 'addkeytovalue' (appends
   * the key to each value for debugging purposes, resulting in: key=value
   * [key])
   *
   * @parameter property="filters"
   */
  private String[] filters;


  /**
   * Sets the basename.
   *
   * @param basename the basename to set
   */
  public void setBasename(String basename) {
    this.basename = basename;
  }

  /**
   * Sets the defaultlanguage.
   *
   * @param defaultlanguage the defaultlanguage to set
   */
  public void setDefaultlanguage(String defaultlanguage) {
    this.defaultlanguage = defaultlanguage;
  }

  /**
   * Sets the filters.
   *
   * @param filters the filters to set
   */
  public void setFilters(String[] filters) {
    this.filters = filters;
  }

  @Override
  protected ExportType getExportType() {
    return ExportType.PROPERTIES;
  }

  @Override
  protected String getBasename() {
    return basename;
  }

  @Override
  protected String getDefaultlanguage() {
    return defaultlanguage;
  }

  @Override
  protected String[] getFilters() {
    return filters;
  }

}
