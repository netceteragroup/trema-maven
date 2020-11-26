package com.netcetera.trema.maven;

import com.netcetera.trema.core.api.IExporter;
import com.netcetera.trema.core.exporting.AndroidExporter;
import com.netcetera.trema.core.exporting.CSVExporter;
import com.netcetera.trema.core.exporting.JsonExporter;
import com.netcetera.trema.core.exporting.OutputStreamFactory;
import com.netcetera.trema.core.exporting.PropertiesExporter;
import com.netcetera.trema.core.exporting.XLSExporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test for {@link ExporterFactory}.
 */
class ExporterFactoryTest {

  private ExporterFactory factory;
  private File file;
  private OutputStreamFactory osFactory;

  @BeforeEach
  void setup() {
    factory = new ExporterFactory();

    file = new File("target/classes/file.name");
    osFactory = Mockito.mock(OutputStreamFactory.class);
  }

  @Test
  void shouldThrowRuntimeExceptionForInvalidType() {
    // given / when / then
    assertThrows(NullPointerException.class,
      () -> factory.getExporter(null, null, null, null));
  }

  @Test
  void shouldCreatePropertyExporter() throws FileNotFoundException, UnsupportedEncodingException {
    // given / when
    IExporter exporter = factory.getExporter(ExportType.PROPERTIES, file, osFactory, null);

    // then
    assertThat(exporter, instanceOf(PropertiesExporter.class));
  }

  @Test
  void shouldCreateJsonExporter() throws FileNotFoundException, UnsupportedEncodingException {
    // given / when
    IExporter exporter = factory.getExporter(ExportType.JSON, file, osFactory, null);

    // then
    assertThat(exporter, instanceOf(JsonExporter.class));
  }

  @Test
  void shouldCreateCsvExporter() throws FileNotFoundException, UnsupportedEncodingException {
    // given
    TremaExportContext context = new TremaExportContext();
    context.setCsvEncoding(Charset.defaultCharset().name());
    context.setCsvSeparator(',');

    // when
    IExporter exporter = factory.getExporter(ExportType.CSV, file, osFactory, context);

    // then
    assertThat(exporter, instanceOf(CSVExporter.class));
  }

  @Test
  void shouldCreateXlsExporter() throws FileNotFoundException, UnsupportedEncodingException {
    // given / when
    IExporter exporter = factory.getExporter(ExportType.XLS, file, osFactory, null);

    // then
    assertThat(exporter, instanceOf(XLSExporter.class));
  }

  @Test
  void shouldCreateAndroidExporter() throws FileNotFoundException, UnsupportedEncodingException {
    // given / when
    IExporter exporter = factory.getExporter(ExportType.ANDROID, file, osFactory, null);

    // then
    assertThat(exporter, instanceOf(AndroidExporter.class));
  }
}
