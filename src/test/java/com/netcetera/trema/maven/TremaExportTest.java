package com.netcetera.trema.maven;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.netcetera.trema.core.Status;
import com.netcetera.trema.core.XMLDatabase;
import com.netcetera.trema.core.api.IExportFilter;
import com.netcetera.trema.core.api.ITextNode;
import com.netcetera.trema.core.exporting.ExportException;
import com.netcetera.trema.core.exporting.JsonExporter;
import com.netcetera.trema.core.exporting.OutputStreamFactory;

/**
 * Test for TremaExport.
 */
public class TremaExportTest {
  private XMLDatabase db;
  private ExporterFactory factory;

  private TremaExport tremaExport;
  private TremaExportContext context;
  private JsonExporter exporter;


  /**
   * setUp().
   *
   * @throws Exception in case setUp fails
   */
  @Before
  public void setUp() throws Exception {
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
  public void generateLanguageJsonWithoutDefaultLanguage() throws IOException, ExportException {
    ITextNode[] nodes = db.getTextNodes();

    BDDMockito.given(factory.getExporter(Matchers.eq(ExportType.JSON), Matchers.any(File.class),
        Matchers.any(OutputStreamFactory.class), Matchers.eq(context))).willReturn(exporter);

    tremaExport.exportAsJson(db);

    Mockito.verify(factory, Mockito.times(2)).getExporter(Matchers.eq(ExportType.JSON), Matchers.any(File.class),
        Matchers.any(OutputStreamFactory.class), Matchers.eq(context));
    Mockito.verify(exporter, Mockito.times(2)).setExportFilter(Mockito.any(IExportFilter[].class));
    Mockito.verify(exporter).export(nodes, db.getMasterLanguage(), "en", new Status[]{Status.INITIAL});
    Mockito.verify(exporter).export(nodes, db.getMasterLanguage(), "de", new Status[]{Status.INITIAL});
    Mockito.verifyNoMoreInteractions(factory, exporter);
  }

  @Test
  public void generateLanguageJsonWithDefaultLanguage() throws IOException, ExportException {
    ITextNode[] nodes = db.getTextNodes();
    context.setDefaultLanguage("en");

    BDDMockito.given(factory.getExporter(Matchers.eq(ExportType.JSON),
        Matchers.any(File.class), Matchers.any(OutputStreamFactory.class), Matchers.eq(context))).willReturn(exporter);

    tremaExport.exportAsJson(db);

    Mockito.verify(factory, Mockito.times(3)).getExporter(
        Matchers.eq(ExportType.JSON), Matchers.any(File.class), Matchers.any(OutputStreamFactory.class),
        Matchers.eq(context));
    Mockito.verify(exporter, Mockito.times(3)).setExportFilter(Mockito.any(IExportFilter[].class));
    Mockito.verify(exporter, Mockito.times(2)).export(
        nodes, db.getMasterLanguage(), "en", new Status[]{Status.INITIAL});
    Mockito.verify(exporter).export(nodes, db.getMasterLanguage(), "de", new Status[]{Status.INITIAL});
    Mockito.verifyNoMoreInteractions(factory, exporter);
  }
}
