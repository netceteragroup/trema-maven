package com.netcetera.trema.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.netcetera.trema.maven.ExportMojoTestUtils.BASENAME;
import static com.netcetera.trema.maven.ExportMojoTestUtils.TREMA_FILE;
import static com.netcetera.trema.maven.TestUtils.isExistingFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test for the property file export mojo ({@link ExportPropertiesMojo}).
 */
class ExportPropertiesMojoTest {

  private static final String PROPERTIES_EXTENSION = ".properties";

  private ExportMojoTestUtils exportMojoTestUtils = new ExportMojoTestUtils(PROPERTIES_EXTENSION);

  /**
   * Test method for {@link com.netcetera.trema.maven.ExportPropertiesMojo#execute()}.
   */
  @Test
  void shouldExecuteSuccessfully() {
    // given / when
    exportMojoTestUtils.executeExportPropertiesMojo(new String[]{"en", "de", "fr"}, new String[]{"verified"});

    // then
    // make sure the files where written
    assertThat(new File("target/classes/test_de.properties"), isExistingFile());
    assertThat(new File("target/classes/test_en.properties"), isExistingFile());
    assertThat(new File("target/classes/test_fr.properties"), isExistingFile());
  }

  @Test
  void executeExportPropertiesMojoReturnsReadableResourceBundle() {
    exportMojoTestUtils.executeExportPropertiesMojo(new String[]{"de", "en"}, new String[]{"verified", "translated"});

    ResourceBundle bundle = ResourceBundle.getBundle("test", Locale.GERMANY);
    assertThat(bundle.getString("test.simple"), equalTo("Test (de)"));
    assertThat(bundle.getString("test.umlaute"), equalTo("ÄöÜ[@"));

    bundle = ResourceBundle.getBundle("test", Locale.ENGLISH);
    assertThat(bundle.getString("test.simple"), equalTo("Test (en)"));
    assertThat(bundle.getString("test.umlaute"), equalTo("ÀÉÈÔô"));
  }

  /**
   * Test with no trema file specification.
   */
  @Test
  void shouldThrowForMissingTremaFile() {
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
    mojo.setBasename(BASENAME);
    mojo.setLanguages(new String[]{"en", "de", "fr"});
    mojo.setStates(new String[]{"verified"});

    MojoExecutionException ex = assertThrows(MojoExecutionException.class, mojo::execute);

    assertThat(ex.getMessage(), equalTo("tremaFile must not be empty"));
  }

  @Test
  void shouldThrowForNonExistentTremaFile() {
    // given
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
    mojo.setTremaFile("src/test/resources/text-nonexistent.trm");
    mojo.setBasename(BASENAME);
    mojo.setLanguages(new String[]{"en", "de", "fr"});
    mojo.setStates(new String[]{"verified"});

    // when / then
    assertThrows(MojoExecutionException.class, mojo::execute);
  }

  @Test
  void shouldThrowForMissingBaseName() {
    // given
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
    mojo.setTremaFile(TREMA_FILE);
    mojo.setLanguages(new String[]{"en", "de", "fr"});
    mojo.setStates(new String[]{"verified"});

    // when
    MojoExecutionException ex = assertThrows(MojoExecutionException.class, mojo::execute);

    // then
    assertThat(ex.getMessage(), equalTo("basename must not be empty"));
  }

  /**
   * Test with no languages.
   *
   * @throws Exception if the test failed
   */
  @Test
  void testNoLanguages() throws Exception {
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
    mojo.setTremaFile(TREMA_FILE);
    mojo.setBasename(BASENAME);
    mojo.setStates(new String[]{"verified"});
    mojo.execute();
  }

  /**
   * Test with empty language configuration.
   *
   * @throws Exception if the test failed
   */
  @Test
  void testEmptyLanguages() throws Exception {
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
    mojo.setTremaFile(TREMA_FILE);
    mojo.setBasename(BASENAME);
    mojo.setLanguages(new String[]{});
    mojo.setStates(new String[]{"verified"});
    mojo.execute();
  }

  /**
   * Test with invalid language configuration.
   *
   * @throws Exception if the test failed
   */
  @Test
  void testInvalidLanguages() throws Exception {
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
    mojo.setTremaFile(TREMA_FILE);
    mojo.setBasename(BASENAME);
    mojo.setLanguages(new String[]{"foo"});
    mojo.setStates(new String[]{"verified"});
    mojo.execute();
  }

  /**
   * Test using the MessageFormat filter.
   *
   * @throws Exception if the test failed
   */
  @Test
  void testMessageFormatFilter() throws Exception {
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
    mojo.setTremaFile(TREMA_FILE);
    mojo.setBasename(BASENAME);
    mojo.setLanguages(new String[]{"foo"});
    mojo.setStates(new String[]{"verified"});
    mojo.setFilters(new String[]{"messageformat"});
    mojo.execute();
  }

  @Test
  void shouldThrowForUnknownFilter() {
    // given
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
    mojo.setTremaFile(TREMA_FILE);
    mojo.setBasename(BASENAME);
    mojo.setLanguages(new String[]{"foo"});
    mojo.setStates(new String[]{"verified"});
    mojo.setFilters(new String[]{"doesntexist"});

    // when
    MojoExecutionException ex = assertThrows(MojoExecutionException.class, mojo::execute);

    // then
    assertThat(ex.getMessage(), equalTo("Invalid Filter arguments: Invalid filter: doesntexist"));
  }

  @Test
  void testDefaultLanguage() throws Exception {
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
    mojo.setTremaFile(TREMA_FILE);
    mojo.setBasename(BASENAME);
    mojo.setLanguages(new String[]{});
    mojo.setDefaultlanguage("de");
    mojo.execute();
  }
}
