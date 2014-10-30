package com.netcetera.trema.maven;


/**
 * Contains all info for an import.
 */
public class TremaImportContext {
  
  /** The default encoding for writing the XML file. */
  public static final String DEFAULT_XML_ENCODING = "UTF-8";
  /** The default line separator for the XML file. */
  public static final String DEFAULT_XML_LINE_SEPARATOR = "\n";
  /** The default CSV separator. */
  public static final char DEFAULT_CSV_SEPARATOR = ';';
  
  private String importFilePathName = null;
  private String databaseFilePathName = null;
  private boolean justAnalyze = false;
  private boolean doNotApplyConflictingChanges = false;
  private String csvEncoding = null;
  private String outputPathName = null;
  private char csvSeparator = DEFAULT_CSV_SEPARATOR;
  private String xmlEncoding = DEFAULT_XML_ENCODING;
  private String xmlLineSeparator = DEFAULT_XML_LINE_SEPARATOR;
  
  /**
   * Gets the importFilePathName.
   * @return Returns the importFilePathName.
   */
  public String getImportFilePathName() {
    return importFilePathName;
  }
  
  /**
   * Sets the importFilePathName.
   * @param importFilePathName The importFilePathName to set.
   */
  public void setImportFilePathName(String importFilePathName) {
    this.importFilePathName = importFilePathName;
  }
  
  /**
   * Gets the databaseFilePathName.
   * @return Returns the databaseFilePathName.
   */
  public String getDatabaseFilePathName() {
    return databaseFilePathName;
  }
  
  /**
   * Sets the databaseFilePathName.
   * @param databaseFilePathName The databaseFilePathName to set.
   */
  public void setDatabaseFilePathName(String databaseFilePathName) {
    this.databaseFilePathName = databaseFilePathName;
  }
  
  /**
   * Gets the justAnalyze.
   * @return Returns the justAnalyze.
   */
  public boolean isJustAnalyze() {
    return justAnalyze;
  }
  
  /**
   * Sets the justAnalyze.
   * @param justAnalyze The justAnalyze to set.
   */
  public void setJustAnalyze(boolean justAnalyze) {
    this.justAnalyze = justAnalyze;
  }
  
  /**
   * Gets the doNotApplyConflictingChanges.
   * @return Returns the doNotApplyConflictingChanges.
   */
  public boolean isDoNotApplyConflictingChanges() {
    return doNotApplyConflictingChanges;
  }
  
  /**
   * Sets the doNotApplyConflictingChanges.
   * @param doNotApplyConflictingChanges The doNotApplyConflictingChanges to set.
   */
  public void setDoNotApplyConflictingChanges(boolean doNotApplyConflictingChanges) {
    this.doNotApplyConflictingChanges = doNotApplyConflictingChanges;
  }
  
  /**
   * Gets the csvEncoding.
   * @return Returns the csvEncoding.
   */
  public String getCsvEncoding() {
    return csvEncoding;
  }
  
  /**
   * Sets the csvEncoding.
   * @param csvEncoding The csvEncoding to set.
   */
  public void setCsvEncoding(String csvEncoding) {
    this.csvEncoding = csvEncoding;
  }
  
  /**
   * Gets the outputPathName.
   * @return Returns the outputPathName.
   */
  public String getOutputPathName() {
    return outputPathName;
  }
  
  /**
   * Sets the outputPathName.
   * @param outputPathName The outputPathName to set.
   */
  public void setOutputPathName(String outputPathName) {
    this.outputPathName = outputPathName;
  }
  
  /**
   * Gets the csvSeparator.
   * @return Returns the csvSeparator.
   */
  public char getCsvSeparator() {
    return csvSeparator;
  }
  
  /**
   * Sets the csvSeparator.
   * @param csvSeparator The csvSeparator to set.
   */
  public void setCsvSeparator(char csvSeparator) {
    this.csvSeparator = csvSeparator;
  }
  
  /**
   * Gets the xmlEncoding.
   * @return Returns the xmlEncoding.
   */
  public String getXmlEncoding() {
    return xmlEncoding;
  }
  
  /**
   * Sets the xmlEncoding.
   * @param xmlEncoding The xmlEncoding to set.
   */
  public void setXmlEncoding(String xmlEncoding) {
    this.xmlEncoding = xmlEncoding;
  }
  
  /**
   * Gets the xmlLineSeparator.
   * @return Returns the xmlLineSeparator.
   */
  public String getXmlLineSeparator() {
    return xmlLineSeparator;
  }
  
  /**
   * Sets the xmlLineSeparator.
   * @param xmlLineSeparator The xmlLineSeparator to set.
   */
  public void setXmlLineSeparator(String xmlLineSeparator) {
    this.xmlLineSeparator = xmlLineSeparator;
  }
  


}
