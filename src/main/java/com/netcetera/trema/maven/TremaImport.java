package com.netcetera.trema.maven;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.maven.plugin.logging.Log;

import com.netcetera.trema.core.ParseException;
import com.netcetera.trema.core.XMLDatabase;
import com.netcetera.trema.core.api.IDatabase;
import com.netcetera.trema.core.api.IImportSource;
import com.netcetera.trema.core.importing.CSVFile;
import com.netcetera.trema.core.importing.Change;
import com.netcetera.trema.core.importing.ChangesAnalyzer;
import com.netcetera.trema.core.importing.XLSFile;


/**
 * Does an import into a trema database file.
 */
public class TremaImport {

  private String importFilePathName;
  private String databaseFilePathName;
  private boolean justAnalyze;
  private boolean doNotApplyConflictingChanges;
  private String csvEncoding;
  private char csvSeparator;
  private String outputPathName;
  private String xmlEncoding;
  private String xmlLineSeparator;
  private Log log;


  /**
   * Constructor.
   *
   * @param importContext the context
   */
  public TremaImport(TremaImportContext importContext, Log log) {
    this.importFilePathName = importContext.getImportFilePathName();
    this.databaseFilePathName = importContext.getDatabaseFilePathName();
    this.justAnalyze = importContext.isJustAnalyze();
    this.doNotApplyConflictingChanges = importContext.isDoNotApplyConflictingChanges();
    this.csvEncoding = importContext.getCsvEncoding();
    this.csvSeparator = importContext.getCsvSeparator();
    this.outputPathName = importContext.getOutputPathName();
    this.xmlEncoding = importContext.getXmlEncoding();
    this.xmlLineSeparator = importContext.getXmlLineSeparator();
    this.log = log;
  }


  /**
   * Executes the import.
   *
   * @throws Exception in case the import failed
   */
  public void execute() throws Exception {
    IImportSource importFile = null;
    try {
      importFile = getImportSource(importFilePathName, csvEncoding, csvSeparator);
    } catch (ParseException e) {
      logAndThrow("Parse error in import file " + importFilePathName + " on line "
          + e.getLineNumber() + ": " + e.getMessage());
    } catch (UnsupportedEncodingException e) {
      logAndThrow("The specified encoding is not supported: " + e.getMessage());
    } catch (IOException e) {
      logAndThrow("Could not open import file file: " + e.getMessage());
    }

    // open and internalize the XML file
    XMLDatabase xmlDb = new XMLDatabase();
    InputStream inputStream = null;
    try {
      inputStream = new FileInputStream(databaseFilePathName);
      xmlDb.build(inputStream, false);
    } catch (ParseException e) {
      StringBuilder message = new StringBuilder("Parse error in XML file " + databaseFilePathName);
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
          logAndThrow("Could not close XML file: " + e.getMessage());
        }
      }
    }

    printSummary(importFile, importFilePathName, xmlDb, databaseFilePathName);

    // analyze the changes
    ChangesAnalyzer analyzer = null;
    try {
      analyzer = new ChangesAnalyzer(importFile, xmlDb);
    } catch (IllegalArgumentException e) {
      logAndThrow(e.getMessage());
    }

    analyzer.analyze();
    Change[] conflictingChanges = analyzer.getConflictingChanges();
    Change[] nonConflictingChanges = analyzer.getNonConflictingChanges();

    print("Conflicting changes: " + conflictingChanges.length);
    print("Non-conflicting changes: " + nonConflictingChanges.length);

    if (conflictingChanges.length > 0) {
      print("Summary of conflicting changes:");
      for (int i = 0; i < conflictingChanges.length; i++) {
        printChange(conflictingChanges[i]);
      }
    }

    if (!justAnalyze) {
      // apply the changes
      print("Applying non-conflicting changes...");
      for (int i = 0; i < nonConflictingChanges.length; i++) {
        ChangesAnalyzer.applyChange(xmlDb, nonConflictingChanges[i]);
      }
      print("done.");

      if (!doNotApplyConflictingChanges) {
        print("Applying conflicting changes...");
        for (int i = 0; i < conflictingChanges.length; i++) {
          ChangesAnalyzer.applyChange(xmlDb, conflictingChanges[i]);
        }
        print("done.");
      }

      print("Writing file " + outputPathName + "...");
      OutputStream outputStream = null;
      try {
        // possibly create the file
        File outFile = new File(outputPathName);
        File parent = outFile.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
          logAndThrow("\nCould not create directory: " + parent.getAbsolutePath());
        }
        boolean didNotExist = outFile.createNewFile();
        if (!didNotExist) {
          log.debug("File '" + outFile.getAbsolutePath() + "' existed already.");
        }
        outputStream = new FileOutputStream(outFile);
        xmlDb.writeXML(outputStream, xmlEncoding, "  ", xmlLineSeparator);
        print("done.");
      } catch (IOException e) {
        logAndThrow("\nCould not write XML file: " + e.getMessage());
      } finally {
        if (outputStream != null) {
          try {
            outputStream.close();
          } catch (IOException e) {
            logAndThrow("Could not write XML file: " + e.getMessage());
          }
        }
      }
    }
  }

  /**
   * Prints a message.
   *
   * @param msg the message
   */
  protected void print(String msg) {
    log.info(msg);
  }

  /**
   * Logs a message and throws an exception.
   *
   * @param msg the msg
   * @throws Exception the Exception
   */
  protected void logAndThrow(String msg) throws Exception {
    throw new Exception(msg);
  }

  /**
   * Prints a summary of a change to a given <code>PrintStream</code>.
   * @param change the change
   */
  protected void printChange(Change change) {
    print("Key: " + change.getKey());
    switch (change.getType()) {
      case Change.TYPE_MASTER_VALUE_CHANGED:
        print("The master value has changed from '" + change.getDbMasterValue()
                            + "' to '" + change.getImportedMasterValue() + "'.");
        break;
      case Change.TYPE_IMPORTED_STATUS_OLDER:
        print("The imported status ('" + change.getImportedStatus() + "') is older than the db status ('"
                            + change.getDbStatus() + "').");
        break;
      case Change.TYPE_IMPORTED_STATUS_NEWER:
        print("The imported status ('" + change.getImportedStatus() + "') is newer than the db status ('"
                            + change.getDbStatus() + "').");
        break;
      case Change.TYPE_LANGUAGE_ADDITION:
        print("The language '" + change.getLanguage() + "' does not exist in the db.");
        break;
      case Change.TYPE_MASTER_LANGUAGE_ADDITION:
        print("The master language '" + change.getMasterLanguage() + "' does not exist in the db.");
        break;
      case Change.TYPE_KEY_ADDITION:
        print("The key has been added (is present in the import file but not in the DB).");
        break;
      case Change.TYPE_VALUE_CHANGED:
        print("The text value has changed from '" + change.getDbValue()
                            + "' to '" + change.getImportedValue() + "'.");
        break;
      case Change.TYPE_VALUE_AND_STATUS_CHANGED:
        print("The text value has changed from '" + change.getDbValue()
                            + "' to '" + change.getImportedValue() + "'.");
        print("The text status has changed from '" + change.getDbStatus()
                            + "' to '" + change.getImportedStatus() + "'.");
        break;
      default:
        print("Unknown change.");
    }

    if (change.isConflicting()) {
      if (change.isAccept()) {
        print("--> Values to be applied: ");
        print("    Master value: " + change.getAcceptMasterValue());
        print("    Status: " + change.getAcceptStatus());
        print("    Value: " + change.getAcceptValue());
      } else {
        print("--> The change will be ignored.");
      }
    }
  }

  /**
   * Prints a short summary of a trema CSV file and a trema XML
   * database to a given <code>PrintWriter</code>.
   * @param csvFile the trema CSV file
   * @param xmlDB the trema XML database
   */
  private void printSummary(IImportSource csvFile, String csvPathname,
                                   IDatabase xmlDB, String xmlPathname) {
    print("Import file:" + csvPathname);
    print("Number of records: " + csvFile.getSize());
    print(", master language: " + csvFile.getMasterLanguage());
    print(", language: " + csvFile.getLanguage());

    print("");
    print("Database file: " + xmlPathname);
    print("Number of records: " + xmlDB.getSize());
    print(", master language: " + xmlDB.getMasterLanguage());
  }

  /**
   * Gets an IImportSource.
   * If the filename ends with .xls an XLSFile instance will be created, otherwise a CSVFile instance.
   *
   * @param fileName the filename
   * @param csvEncoding the csv encoding (only needed for csv files)
   * @param csvSeparator the csv separator (only needed for csv files)
   * @return an instance of IImportSource
   * @throws IllegalArgumentException in case the filename is null
   * @throws ParseException in case the file cannot be parsed
   * @throws IOException in case the file cannot be opened/read
   */
  private static IImportSource getImportSource(String fileName,  String csvEncoding, char csvSeparator)
  throws IllegalArgumentException, ParseException, IOException {
    if (fileName == null) {
      throw new IllegalArgumentException("filename must not be null");
    }
    if (fileName.endsWith(".xls")) {
      return new XLSFile(fileName);
    } else {
      return new CSVFile(fileName, csvEncoding, csvSeparator);
    }
  }

}

