package com.netcetera.trema.maven;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.netcetera.trema.core.api.IExportFilter;
import com.netcetera.trema.core.exporting.AddKeyToValueExportFilter;
import com.netcetera.trema.core.exporting.HtmlLineBreakConverter;
import com.netcetera.trema.core.exporting.MessageFormatEscapingFilter;

/**
 * Test class.
 */
public class TremaExportContextTest {

  private TremaExportContext context;
  @Before
  public void setup() {
    context = new TremaExportContext();
  }
  @Test
  public void shouldNotSetFiltersForNullArgument() {
    Assert.assertNull(context.getFilters());

    context.setFilters(null);

    Assert.assertNull(context.getFilters());
  }
  @Test
  public void shouldAddMessageEscapingFilter() {
    context.setFilters(new String[]{TremaExportContext.FILTER_TYPE_MESSAGE_FORMAT});

    IExportFilter[] filters = context.getFilters();

    Assert.assertEquals(1, filters.length);
    Assert.assertTrue(filters[0] instanceof MessageFormatEscapingFilter);
  }

  @Test
  public void shouldAddKeyToValueFilter() {
    context.setFilters(new String[]{TremaExportContext.FILTER_TYPE_ADD_KEY_TO_VALUE});

    IExportFilter[] filters = context.getFilters();

    Assert.assertEquals(1, filters.length);
    Assert.assertTrue(filters[0] instanceof AddKeyToValueExportFilter);
  }

  @Test
  public void shouldAddHtmlLineBreakFilter() {
    context.setFilters(new String[]{TremaExportContext.FILTER_TYPE_REPLACE_WITH_HTML_NEWLINE});

    IExportFilter[] filters = context.getFilters();

    Assert.assertEquals(1, filters.length);
    Assert.assertTrue(filters[0] instanceof HtmlLineBreakConverter);
  }
  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionOnArbitraryString() {
    context.setFilters(new String[]{"blah"});
  }
  @Test
  public void numberOfFiltersShouldMatchNumberOfArguments() {
    context.setFilters(new String[]{TremaExportContext.FILTER_TYPE_REPLACE_WITH_HTML_NEWLINE,
        TremaExportContext.FILTER_TYPE_ADD_KEY_TO_VALUE,
        TremaExportContext.FILTER_TYPE_ADD_KEY_TO_VALUE});

    IExportFilter[] filters = context.getFilters();

    Assert.assertEquals(3, filters.length);
  }
}
