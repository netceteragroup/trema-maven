package ch.netcetera.trema.maven;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ch.netcetera.trema.common.TremaUtil;
import ch.netcetera.trema.core.ParseException;
import ch.netcetera.trema.core.Status;
import ch.netcetera.trema.core.XMLDatabase;
import ch.netcetera.trema.core.exporting.AndroidExporter;
import ch.netcetera.trema.core.exporting.CSVExporter;
import ch.netcetera.trema.core.exporting.ExportException;
import ch.netcetera.trema.core.exporting.FileOutputStreamFactory;
import ch.netcetera.trema.core.exporting.JsonExporter;
import ch.netcetera.trema.core.exporting.OutputStreamFactory;
import ch.netcetera.trema.core.exporting.PropertiesExporter;
import ch.netcetera.trema.core.exporting.TremaCSVPrinter;
import ch.netcetera.trema.core.exporting.XLSExporter;


/**
 * Does an export from a trema database.
 */
public class TremaExport {

  private ExportType type;
  private String[] languages;
  private Status[] status;
  private String xmlPathName;
  private String baseName;
  private TremaExportContext exportContext;
  private OutputStreamFactory outputStreamFactory = new FileOutputStreamFactory();
  private ExporterFactory exporterFactory;


  /**
   * Constructor.
   *
   * @param exportContext the export context
   */
  public TremaExport(TremaExportContext exportContext) {
    type = exportContext.getType();
    languages = exportContext.getLanguages();
    status = exportContext.getStatus();
    xmlPathName = exportContext.getXmlPathName();
    baseName = exportContext.getBaseName();
    this.exportContext = exportContext;
    exporterFactory = new ExporterFactory();
  }
  // visibility for Testing
  void setExporterFactory(ExporterFactory exporterFactory) {
    this.exporterFactory = exporterFactory;
  }

  /**
   * Does the export.
   *
   * @throws Exception in case the export failed
   */
  public void execute() throws Exception {
    // open and internalize the XML file
    XMLDatabase xmlDb = parseTremaXmlFile();

    if (languages == null) {
      // the user did not specify the language option, so use all languages of
      // the database
      Set<String> languageSet = TremaUtil.getLanguages(xmlDb.getTextNodes());
      languages = languageSet.toArray(new String[languageSet.size()]);
    }

    if (type == ExportType.PROPERTIES) {
      // export properties files
      exportAsProperties(xmlDb);

    } else if (type == ExportType.JSON) {
      // Implicit arguments:
      // languages, baseName, outputStreamFactory, exportContext, status
      exportAsJson(xmlDb);

    } else if (type == ExportType.CSV) {
      // export CSV files
      exportAsCsv(xmlDb);

    } else if (type == ExportType.XLS) {
      // export to xls files
      exportAsXls(xmlDb);

    } else if (type == ExportType.ANDROID) {
      // export android files
      exportAsAndroidFile(xmlDb);
    }
  }
  private XMLDatabase parseTremaXmlFile() throws Exception {
    XMLDatabase xmlDb = new XMLDatabase();
    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(xmlPathName);
      xmlDb.build(inputStream, false);
    } catch (ParseException e) {
      StringBuilder message = new StringBuilder("Parse error in XML file " + xmlPathName);
      if (e.getLineNumber() >= 1) {
        message.append(" on line ").append(e.getLineNumber());
      }
      message.append(": ").append(e.getMessage());

      logAndThrow(message.toString());
    } catch (IOException e) {
      logAndThrow("Could not open XML file: " + e.getMessage());
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          logAndThrow("Could not open XML file: " + e.getMessage());
        }
      }
    }
    return xmlDb;
  }

  private void exportAsAndroidFile(XMLDatabase xmlDb) throws IOException, ExportException {
    String defaultLanguage = exportContext.getDefaultLanguage();

    // get export filenames
    Map<String, String> exportFilenames = new HashMap<String, String>();
    for (int i = 0; i < languages.length; i++) {
      String language = languages[i];
      String sufix = defaultLanguage != null && defaultLanguage.equals(language) ? "" : "-"
          + language;
      String fileName = baseName + "/values" + sufix + "/strings.xml";
      exportFilenames.put(language, fileName);
    }
    // do the export
    for (int i = 0; i < languages.length; i++) {
      String language = languages[i];
      String fileName = exportFilenames.get(language);
      if (fileName == null || "".equals(fileName)) {
        throw new IllegalArgumentException("no filename defined for language:" + language);
      }
      AndroidExporter exporter = new AndroidExporter(getNewFile(fileName), outputStreamFactory);
      print("Writing strings.xml file " + fileName + "...");
      exporter.export(xmlDb.getTextNodes(), xmlDb.getMasterLanguage(), languages[i], status);
      print("done.");
    }
  }


  private void exportAsProperties(XMLDatabase xmlDb) throws IOException, ExportException {
    for (int i = 0; i < languages.length; i++) {
      String fileName = baseName + "_" + languages[i] + ".properties";
      PropertiesExporter exporter = new PropertiesExporter(getNewFile(fileName),
          outputStreamFactory);
      exporter.setExportFilter(exportContext.getFilters());
      print("Writing properties file " + fileName + "...");
      exporter.export(xmlDb.getTextNodes(), xmlDb.getMasterLanguage(), languages[i], status);
      print("done.");
    }
    // export default properties file (without language suffix)
    if (exportContext.getDefaultLanguage() != null) {
      String fileName = baseName + ".properties";
      PropertiesExporter exporter = new PropertiesExporter(getNewFile(fileName),
          outputStreamFactory);
      exporter.setExportFilter(exportContext.getFilters());
      print("Writing default properties file (" + exportContext.getDefaultLanguage() + ") "
          + fileName + "...");
      exporter.export(xmlDb.getTextNodes(), xmlDb.getMasterLanguage(),
          exportContext.getDefaultLanguage(), status);
      print("done.");
    }
  }


  private void exportAsXls(XMLDatabase xmlDb) throws Exception {
    for (int i = 0; i < languages.length; i++) {
      String fileName = baseName + "_" + languages[i] + ".xls";
      print("Writing XLS file " + fileName + "...");
      try {
        XLSExporter exporter = new XLSExporter(getNewFile(fileName));
        exporter.export(xmlDb.getTextNodes(), xmlDb.getMasterLanguage(), languages[i], status);
        print("done.");
      } catch (IOException e) {
        logAndThrow("\nCould not write output: " + e.getMessage());
      } catch (ExportException e) {
        logAndThrow("\nCould not export trema database: " + e.getMessage());
      }
    }
  }

  private void exportAsCsv(XMLDatabase xmlDb) throws Exception {
    for (int i = 0; i < languages.length; i++) {
      String fileName = baseName + "_" + languages[i] + ".csv";
      print("Writing CSV file " + fileName + "...");

      @SuppressWarnings("resource")
      Writer writer = null;
      try {
        writer = new OutputStreamWriter(new FileOutputStream(getNewFile(fileName)),
            exportContext.getCsvEncoding());
        TremaCSVPrinter printer = new TremaCSVPrinter(writer, exportContext.getCsvSeparator());
        CSVExporter exporter = new CSVExporter(printer);
        exporter.export(xmlDb.getTextNodes(), xmlDb.getMasterLanguage(), languages[i], status);
        print("done.");
      } catch (UnsupportedEncodingException e) {
        logAndThrow("\n" + e.getMessage() + " is an unsupported encoding.");
      } catch (IOException e) {
        logAndThrow("\nCould not write output: " + e.getMessage());
      } finally {
        if (writer != null) {
          try {
            writer.close();
          } catch (IOException e) {
            logAndThrow("Could not write output: " + e.getMessage());
          }
        }
      }
    }
  }

  /**
   * Export the database as JSON file.
   * @param xmlDb trema file model
   * @throws java.io.IOException file could not be read
   * @throws ch.netcetera.trema.core.exporting.ExportException could not execute the export
   */
  // Visible for testing
  void exportAsJson(XMLDatabase xmlDb) throws IOException, ExportException {
    for (int i = 0; i < languages.length; i++) {
      String fileName = baseName + "_" + languages[i] + ".json";
      JsonExporter exporter =
          (JsonExporter) exporterFactory.getExporter(ExportType.JSON, getNewFile(fileName),
          outputStreamFactory, exportContext);
      exporter.setExportFilter(exportContext.getFilters());
      print("Writing json file " + fileName + "...");
      exporter.export(xmlDb.getTextNodes(), xmlDb.getMasterLanguage(), languages[i], status);
      print("done.");
    }
    // export default properties file (without language suffix)
    if (exportContext.getDefaultLanguage() != null) {
      String fileName = baseName + ".json";
      JsonExporter exporter =
          (JsonExporter) exporterFactory.getExporter(ExportType.JSON, getNewFile(fileName),
          outputStreamFactory, exportContext);
      exporter.setExportFilter(exportContext.getFilters());
      print("Writing default JSON file (" + exportContext.getDefaultLanguage() + ") "
          + fileName + "...");
      exporter.export(xmlDb.getTextNodes(), xmlDb.getMasterLanguage(),
          exportContext.getDefaultLanguage(), status);
      print("done.");
    }
  }

  private File getNewFile(String fileName) throws IOException {
    // possibly create the file
    File file = new File(fileName);
    print(file.getAbsolutePath());
    File parent = file.getParentFile();
    if (parent != null && !parent.exists() && !parent.mkdirs()) {
      throw new IOException("\nCould not create directory: " + parent.getAbsolutePath());
    }
    boolean success = file.createNewFile();
    if (!success) {
      print("File '" + file.getAbsolutePath() + "' existed already.");
    }
    return file;

  }

  public void setOutputStreamFactory(OutputStreamFactory outputStreamFactory) {
    this.outputStreamFactory = outputStreamFactory;
  }
  /**
   * Logs a msg and throws an Exception containing the msg.
   *
   * @param msg the msg
   * @throws Exception the Exception.
   */
  protected void logAndThrow(String msg) throws Exception {
    throw new Exception(msg);
  }
  /**
   * Prints a message.
   *
   * @param msg the msg
   */
  protected void print(String msg) {
    System.out.println(msg);
  }

}
