package com.netcetera.trema.maven;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import com.netcetera.trema.core.api.IExporter;
import com.netcetera.trema.core.exporting.AndroidExporter;
import com.netcetera.trema.core.exporting.CSVExporter;
import com.netcetera.trema.core.exporting.JsonExporter;
import com.netcetera.trema.core.exporting.OutputStreamFactory;
import com.netcetera.trema.core.exporting.PropertiesExporter;
import com.netcetera.trema.core.exporting.TremaCSVPrinter;
import com.netcetera.trema.core.exporting.XLSExporter;


/**
 * Factory for the to provide the different exporters.
 */
public class ExporterFactory {
  /**
   * Create a new instance of an exporter for a given type.
   * @param type constant as defined in TremaExportContext
   * @param file output file
   * @param osFactory factory for providing the output stream
   * @param  exportContext trema export context
   * @return exporter instance
   * @throws java.io.FileNotFoundException <code>file</code> does not exist
   * @throws java.io.UnsupportedEncodingException wrong character encoding
   */
  public IExporter getExporter(ExportType type, File file, OutputStreamFactory osFactory,
      TremaExportContext exportContext)
      throws FileNotFoundException, UnsupportedEncodingException {
    switch (type) {
      case PROPERTIES:
        return new PropertiesExporter(file, osFactory);

      case JSON:
        return new JsonExporter(file, osFactory);

      case CSV:
        Writer writer = new OutputStreamWriter(new FileOutputStream(file),
            exportContext.getCsvEncoding());
        TremaCSVPrinter printer = new TremaCSVPrinter(writer, exportContext.getCsvSeparator());
        return new CSVExporter(printer);

      case XLS:
        return new XLSExporter(file);

      case ANDROID:
        return new AndroidExporter(file, osFactory);

      default:
        throw new IllegalArgumentException("Unknown/unhandled export type: " + type);
    }
  }
}
