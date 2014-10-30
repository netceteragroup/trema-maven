package ch.netcetera.trema.maven;


/**
 * Export data from a Trema file into android strings.xml files.
 *
 * @goal exportAndroid
 * @phase process-sources
 * @threadSafe
 */
public class ExportAndroidMojo extends AbstractExportMojo {

  /**
   * Path to the res/ directory of android project. The values directories names
   * will be generated in format values-$language$ where language is the
   * specific language that is being exported. The generated files will be named
   * strings.xml
   *
   * @parameter property="exportPath"
   * default-value="${project.build.directory}/generated/texts/"
   */
  private String exportPath;

  /**
   * Defaultlanguage. If specified, this language will be exported into a
   * default file without the language suffix in the values directory name, eg.
   * /values/strings.xml.
   *
   * @parameter property="defaultlanguage"
   */
  private String defaultlanguage;

  /**
   * Sets the exportPath.
   *
   * @param exportPath the exportPath to set
   */
  public void setExportPath(String exportPath) {
    this.exportPath = exportPath;
  }

  /**
   * Sets the defaultlanguage.
   *
   * @param defaultlanguage the defaultlanguage to set
   */
  public void setDefaultlanguage(String defaultlanguage) {
    this.defaultlanguage = defaultlanguage;
  }


  @Override
  protected ExportType getExportType() {
    return ExportType.ANDROID;
  }

  @Override
  protected String getBasename() {
    return exportPath;
  }

  @Override
  protected String getDefaultlanguage() {
    return defaultlanguage;
  }

  @Override
  protected String[] getFilters() {
    return null;
  }

}
