package com.netcetera.trema.maven;

import com.netcetera.trema.core.api.IExportFilter;
import com.netcetera.trema.core.exporting.AddKeyToValueExportFilter;
import com.netcetera.trema.core.exporting.HtmlLineBreakConverter;
import com.netcetera.trema.core.exporting.MessageFormatEscapingFilter;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test for {@link TremaExportContext}.
 */
class TremaExportContextTest {

  private TremaExportContext context = new TremaExportContext();

  @Test
  void shouldNotSetFiltersForNullArgument() {
    // given / when
    IExportFilter[] initialFilters = context.getFilters();
    context.setFilters(null);

    // then
    assertThat(initialFilters, nullValue());
    assertThat(context.getFilters(), nullValue());
  }

  @Test
  void shouldAddMessageEscapingFilter() {
    // given / when
    context.setFilters(new String[]{TremaExportContext.FILTER_TYPE_MESSAGE_FORMAT});

    // then
    IExportFilter[] filters = context.getFilters();
    assertThat(filters, arrayWithSize(1));
    assertThat(filters[0], instanceOf(MessageFormatEscapingFilter.class));
  }

  @Test
  void shouldAddKeyToValueFilter() {
    // given / when
    context.setFilters(new String[]{TremaExportContext.FILTER_TYPE_ADD_KEY_TO_VALUE});

    // then
    IExportFilter[] filters = context.getFilters();
    assertThat(filters, arrayWithSize(1));
    assertThat(filters[0], instanceOf(AddKeyToValueExportFilter.class));
  }

  @Test
  void shouldAddHtmlLineBreakFilter() {
    // given / when
    context.setFilters(new String[]{TremaExportContext.FILTER_TYPE_REPLACE_WITH_HTML_NEWLINE});

    // then
    IExportFilter[] filters = context.getFilters();
    assertThat(filters, arrayWithSize(1));
    assertThat(filters[0], instanceOf(HtmlLineBreakConverter.class));
  }

  @Test
  void shouldThrowExceptionOnArbitraryString() {
    // given / when
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> context.setFilters(new String[]{"blah"}));

    // then
    assertThat(ex.getMessage(), equalTo("Invalid filter: blah"));
  }

  @Test
  void shouldAddMultipleFilters() {
    // given
    String[] filterNames = {
      TremaExportContext.FILTER_TYPE_REPLACE_WITH_HTML_NEWLINE,
      TremaExportContext.FILTER_TYPE_ADD_KEY_TO_VALUE,
      TremaExportContext.FILTER_TYPE_ADD_KEY_TO_VALUE};

    // when
    context.setFilters(filterNames);

    // then
    assertThat(context.getFilters(), arrayWithSize(3));
  }
}
