package com.netcetera.trema.maven;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.netcetera.trema.core.api.IExporter;
import com.netcetera.trema.core.exporting.AndroidExporter;
import com.netcetera.trema.core.exporting.CSVExporter;
import com.netcetera.trema.core.exporting.JsonExporter;
import com.netcetera.trema.core.exporting.OutputStreamFactory;
import com.netcetera.trema.core.exporting.PropertiesExporter;
import com.netcetera.trema.core.exporting.XLSExporter;

/**
 * Test for the exporter factory.
 */
public class ExporterFactoryTest {

  private ExporterFactory factory;
  private File file;
  private OutputStreamFactory osFactory;
  @Before
  public void setup() {
    factory = new ExporterFactory();

    file = new File("target/classes/file.name");
    osFactory = Mockito.mock(OutputStreamFactory.class);
  }

  @Test(expected = RuntimeException.class)
  public void shouldThrowRuntimeExceptionForInvalidType() throws FileNotFoundException, UnsupportedEncodingException {
    factory.getExporter(null, null, null, null);
  }
  @Test
  public void shouldCreatePropertyExporter() throws FileNotFoundException, UnsupportedEncodingException {
    IExporter exporter = factory.getExporter(ExportType.PROPERTIES, file, osFactory, null);
    Assert.assertTrue(exporter instanceof PropertiesExporter);
  }
  @Test
  public void shouldCreateJsonExporter() throws FileNotFoundException, UnsupportedEncodingException {
    IExporter exporter = factory.getExporter(ExportType.JSON, file, osFactory, null);
    Assert.assertTrue(exporter instanceof JsonExporter);
  }
  @Test
  public void shouldCreateCSVExporter() throws FileNotFoundException, UnsupportedEncodingException {
    TremaExportContext context = new TremaExportContext();
    context.setCsvEncoding(Charset.defaultCharset().name());
    context.setCsvSeparator(',');

    IExporter exporter = factory.getExporter(ExportType.CSV, file, osFactory, context);
    Assert.assertTrue(exporter instanceof CSVExporter);
  }
  @Test
  public void shouldCreateXLSExporter() throws FileNotFoundException, UnsupportedEncodingException {
    IExporter exporter = factory.getExporter(ExportType.XLS, file, osFactory, null);
    Assert.assertTrue(exporter instanceof XLSExporter);
  }
  @Test
  public void shouldCreateAndroidExporter() throws FileNotFoundException, UnsupportedEncodingException {
    IExporter exporter = factory.getExporter(ExportType.ANDROID, file, osFactory, null);
    Assert.assertTrue(exporter instanceof AndroidExporter);
  }

}
