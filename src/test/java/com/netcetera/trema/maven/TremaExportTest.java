package com.netcetera.trema.maven;

import com.netcetera.trema.core.Status;
import com.netcetera.trema.core.XMLDatabase;
import com.netcetera.trema.core.api.IExportFilter;
import com.netcetera.trema.core.api.ITextNode;
import com.netcetera.trema.core.exporting.ExportException;
import com.netcetera.trema.core.exporting.JsonExporter;
import com.netcetera.trema.core.exporting.OutputStreamFactory;
import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Test for {@link TremaExport}.
 */
class TremaExportTest {

  private XMLDatabase db;
  private ExporterFactory factory;

  private TremaExport tremaExport;
  private TremaExportContext context;
  private JsonExporter exporter;

  @BeforeEach
  void setUp() throws Exception {
    db = new XMLDatabase();
    db.build(
        "<?xml version='1.0' encoding='UTF-8'?><trema masterLang='de'>"
            + "<text key=\"com.netcetera.angular.translation.title\">"
            + "<context/>"
            + "<value lang=\"en\" status=\"initial\">Hello World</value>"
            + "<value lang=\"de\" status=\"initial\">Hallo Welt</value>"
            + "</text>"
            + "<text key=\"com.netcetera.angular.translation.language.en\">"
            + "<context/>"
            + "<value lang=\"en\" status=\"initial\">English</value>"
            + "<value lang=\"de\" status=\"initial\">Englisch</value>"
            + "</text>"
            + "<text key=\"com.netcetera.angular.translation.language.de\">"
            + "<context/>"
            + "<value lang=\"en\" status=\"initial\">German</value>"
            + "<value lang=\"de\" status=\"initial\">Deutsch</value>"
            + "</text>"
            + "<text key=\"com.netcetera.angular.translation.test\">"
            + "<context/>"
            + "<value lang=\"en\" status=\"initial\">Test</value>"
            + "</text>"
            + "</trema>", false
    );
    exporter = Mockito.mock(JsonExporter.class);
    factory = Mockito.mock(ExporterFactory.class);
    context = new TremaExportContext();
    context.setType(ExportType.JSON);
    context.setLanguages(new String[]{"en", "de"});
    context.setStatus(new String[]{"initial"});
    context.setXmlPathName("xmlPathName");
    context.setBaseName("target/classes/baseName");
    context.setFilters(new String[]{TremaExportContext.FILTER_TYPE_MESSAGE_FORMAT,
        TremaExportContext.FILTER_TYPE_ADD_KEY_TO_VALUE});

    tremaExport = new TremaExport(context, Mockito.mock(Log.class));
    tremaExport.setExporterFactory(factory);
  }

  @Test
  void generateLanguageJsonWithoutDefaultLanguage() throws IOException, ExportException {
    ITextNode[] nodes = db.getTextNodes();

    given(factory.getExporter(eq(ExportType.JSON), any(File.class),
        any(OutputStreamFactory.class), eq(context))).willReturn(exporter);

    tremaExport.exportAsJson(db);

    verify(factory, times(2)).getExporter(eq(ExportType.JSON), any(File.class),
        any(OutputStreamFactory.class), eq(context));
    verify(exporter, times(2)).setExportFilter(any(IExportFilter[].class));
    verify(exporter).export(nodes, db.getMasterLanguage(), "en", new Status[]{Status.INITIAL});
    verify(exporter).export(nodes, db.getMasterLanguage(), "de", new Status[]{Status.INITIAL});
    verifyNoMoreInteractions(factory, exporter);
  }

  @Test
  void generateLanguageJsonWithDefaultLanguage() throws IOException, ExportException {
    ITextNode[] nodes = db.getTextNodes();
    context.setDefaultLanguage("en");

    given(factory.getExporter(eq(ExportType.JSON),
        any(File.class), any(OutputStreamFactory.class), eq(context))).willReturn(exporter);

    tremaExport.exportAsJson(db);

    verify(factory, times(3)).getExporter(
        eq(ExportType.JSON), any(File.class), any(OutputStreamFactory.class),
        eq(context));
    verify(exporter, times(3)).setExportFilter(any(IExportFilter[].class));
    verify(exporter, times(2)).export(
        nodes, db.getMasterLanguage(), "en", new Status[]{Status.INITIAL});
    verify(exporter).export(nodes, db.getMasterLanguage(), "de", new Status[]{Status.INITIAL});
    verifyNoMoreInteractions(factory, exporter);
  }
}
