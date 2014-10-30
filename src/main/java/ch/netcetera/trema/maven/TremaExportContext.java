package ch.netcetera.trema.maven;

import ch.netcetera.trema.core.Status;
import ch.netcetera.trema.core.api.IExportFilter;
import ch.netcetera.trema.core.exporting.AddKeyToValueExportFilter;
import ch.netcetera.trema.core.exporting.HtmlLineBreakConverter;
import ch.netcetera.trema.core.exporting.MessageFormatEscapingFilter;


/**
 * Contains all info for an export.
 */
public class TremaExportContext {

  /** The option argument name for a CSV export. */
  public static final String TYPE_CSV_NAME = "csv";

  /** The option argument name for a CSV export. */
  public static final String TYPE_XLS_NAME = "xls";

  /** The option argument name for a properties export. */
  public static final String TYPE_PROPERTIES_NAME = "prop";

  /** Apply Message Format Filter. */
  public static final String FILTER_TYPE_MESSAGE_FORMAT = "messageformat";

  /** Apply AddKeyToValue Filter. */
  public static final String FILTER_TYPE_ADD_KEY_TO_VALUE = "addkeytovalue";

  /** Newlines should be replaced with html newlines. */
  public static final String FILTER_TYPE_REPLACE_WITH_HTML_NEWLINE = "replacehtmlnewline";



  /**
   * The default export status. Value is <code>null</code>, which stands for
   * exporting all status.
   */
  private static final Status[] DEFAULT_EXPORT_STATUS = null; // meaning
                                                             // exporting all
                                                             // status

  /** The default CSV separator. */
  public static final char DEFAULT_CSV_SEPARATOR = ';';

  private ExportType type = null;
  private String[] languages = null;
  private Status[] status = DEFAULT_EXPORT_STATUS;
  private IExportFilter[] filters;
  private String xmlPathName = null;
  private String baseName = null;
  private String csvEncoding = null;
  private String defaultLanguage = null;

  private char csvSeparator = DEFAULT_CSV_SEPARATOR;
  private boolean createDefaultProperties = false;

  public boolean isCreateDefaultProperties() {
    return createDefaultProperties;
  }


  public void setCreateDefaultProperties(boolean createDefaultProperties) {
    this.createDefaultProperties = createDefaultProperties;
  }

  /**
   * Gets the type.
   *
   * @return Returns the type.
   */
  public ExportType getType() {
    return type;
  }

  /**
   * Sets the type.
   *
   * @param type The type to set.
   */
  public void setType(ExportType type) {
    this.type = type;
  }

  /**
   * Gets the languages.
   *
   * @return Returns the languages.
   */
  public String[] getLanguages() {
    return languages;
  }

  /**
   * Sets the languages.
   *
   * @param languages The languages to set.
   */
  public void setLanguages(String[] languages) {
    this.languages = languages;
  }

  /**
   * Gets the status.
   *
   * @return Returns the status.
   */
  public Status[] getStatus() {
    return status;
  }

  /**
   * Sets the status.
   *
   * @param statusStr The status to set.
   * @throws IllegalArgumentException in case a status string cannot be
   * converted to a valid status
   */
  public void setStatus(String[] statusStr) throws IllegalArgumentException {
    if (statusStr != null && statusStr.length > 0) {
      status = new Status[statusStr.length];
      for (int i = 0; i < statusStr.length; i++) {
        Status givenStatus = Status.valueOf(statusStr[i].trim());
        if (givenStatus == Status.UNDEFINED) {
          throw new IllegalArgumentException("Invalid status: " + statusStr[i]);
        } else {
          status[i] = givenStatus;
        }
      }
    }
  }

  /**
   * Gets the xmlPathName.
   *
   * @return Returns the xmlPathName.
   */
  public String getXmlPathName() {
    return xmlPathName;
  }

  /**
   * Sets the xmlPathName.
   *
   * @param xmlPathName The xmlPathName to set.
   */
  public void setXmlPathName(String xmlPathName) {
    this.xmlPathName = xmlPathName;
  }

  /**
   * Gets the baseName.
   *
   * @return Returns the baseName.
   */
  public String getBaseName() {
    return baseName;
  }

  /**
   * Sets the baseName.
   *
   * @param baseName The baseName to set.
   */
  public void setBaseName(String baseName) {
    this.baseName = baseName;
  }

  /**
   * Gets the csvEncoding.
   *
   * @return Returns the csvEncoding.
   */
  public String getCsvEncoding() {
    return csvEncoding;
  }

  /**
   * Sets the csvEncoding.
   *
   * @param csvEncoding The csvEncoding to set.
   */
  public void setCsvEncoding(String csvEncoding) {
    this.csvEncoding = csvEncoding;
  }

  /**
   * Gets the csvSeparator.
   *
   * @return Returns the csvSeparator.
   */
  public char getCsvSeparator() {
    return csvSeparator;
  }

  /**
   * Sets the csvSeparator.
   *
   * @param csvSeparator The csvSeparator to set.
   */
  public void setCsvSeparator(char csvSeparator) {
    this.csvSeparator = csvSeparator;
  }

  /**
   * Gets the filters to be applied while exporting Properties.
   *
   * @return the filters
   */
  public IExportFilter[] getFilters() {
    return filters;
  }


  /**
   * Sets the filters to be applied while exporting Properties.
   *
   * @param filterStr the filters
   * @throws IllegalArgumentException in case a filter String cannot be
   * converted to a valid filter
   */
  public void setFilters(String[] filterStr) throws IllegalArgumentException {
    if (filterStr != null) {
      filters = new IExportFilter[filterStr.length];
      for (int i = 0; i < filterStr.length; i++) {
        if (TremaExportContext.FILTER_TYPE_MESSAGE_FORMAT.equalsIgnoreCase(filterStr[i])) {
          filters[i] = new MessageFormatEscapingFilter();
        } else if (TremaExportContext.FILTER_TYPE_ADD_KEY_TO_VALUE.equalsIgnoreCase(filterStr[i])) {
          filters[i] = new AddKeyToValueExportFilter();
        } else if (TremaExportContext.FILTER_TYPE_REPLACE_WITH_HTML_NEWLINE.equalsIgnoreCase(filterStr[i])) {
          filters[i] = new HtmlLineBreakConverter();
        } else {
          throw new IllegalArgumentException("Invalid filter: " + filterStr[i]);
        }
      }
    }
  }

  /**
   * Gets the default language.
   *
   * @return the defaultLanguage
   */
  public String getDefaultLanguage() {
    return defaultLanguage;
  }

  /**
   * If set, this language will be used to create the default properties file
   * without language extension. Must be on of the languages used in the trema
   * database.
   *
   * @param defaultLanguage the defaultLanguage
   */
  public void setDefaultLanguage(String defaultLanguage) {
    this.defaultLanguage = defaultLanguage;
  }

}
