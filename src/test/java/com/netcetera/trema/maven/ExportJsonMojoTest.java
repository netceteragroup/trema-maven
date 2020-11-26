package com.netcetera.trema.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;

import static com.netcetera.trema.maven.TestUtils.isExistingFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test for the json file export mojo ({@link ExportJsonMojo}).
 */
class ExportJsonMojoTest {

  private static final String EXTENSION = ".json";

  private ExportMojoTestUtils exportMojoTestUtils = new ExportMojoTestUtils(EXTENSION);

  private String tremaFilePath;

  @BeforeEach
  void setup() {
    URL url = getClass().getClassLoader().getResource("text.trm");
    tremaFilePath = url.getFile();
  }

  @Test
  void shouldExecuteSuccessfully() {
    // given / when
    exportMojoTestUtils.executeExportJsonMojo(new String[]{"en", "de", "fr"}, new String[]{"verified"});

    // make sure the files where written
    assertThat(new File("target/classes/test_de.json"), isExistingFile());
    assertThat(new File("target/classes/test_en.json"), isExistingFile());
    assertThat(new File("target/classes/test_fr.json"), isExistingFile());
  }

  /**
   * Test with no trema file specification.
   */
  @Test
  void shouldThrowForMissingTremaFile() {
    // given
    final ExportJsonMojo mojo = new ExportJsonMojo();
    mojo.setBasename("target/classes/test");
    mojo.setLanguages(new String[]{"en", "de", "fr"});
    mojo.setStates(new String[]{"verified"});

    // when
    MojoExecutionException ex = assertThrows(MojoExecutionException.class, mojo::execute);

    // then
    assertThat(ex.getMessage(), equalTo("tremaFile must not be empty"));
  }

  @Test
  void shouldThrowForNonExistentTremaFile() {
    // given
    final ExportJsonMojo mojo = new ExportJsonMojo();
    mojo.setTremaFile("src/test/resources/text-nonexistent.trm");
    mojo.setBasename("target/classes/test");
    mojo.setLanguages(new String[]{"en", "de", "fr"});
    mojo.setStates(new String[]{"verified"});

    // when / then
    assertThrows(MojoExecutionException.class, mojo::execute);
  }

  @Test
  void shouldThrowForMissingBasename() {
    // given
    final ExportJsonMojo mojo = new ExportJsonMojo();
    mojo.setTremaFile("src/test/resources/text.trm");
    mojo.setLanguages(new String[]{"en", "de", "fr"});
    mojo.setStates(new String[]{"verified"});

    // when
    MojoExecutionException ex = assertThrows(MojoExecutionException.class, mojo::execute);

    // then
    assertThat(ex.getMessage(), equalTo("basename must not be empty"));
  }

  @Test
  void shouldExecuteWithNullLanguageSet() throws Exception {
    // given
    final ExportJsonMojo mojo = new ExportJsonMojo();
    mojo.setTremaFile(tremaFilePath);
    mojo.setBasename("target/classes/test");
    mojo.setStates(new String[]{"verified"});

    // when
    mojo.execute();

    // then - no exception
  }

  @Test
  void shouldExecuteWithEmptyLanguageSet() throws Exception {
    // given
    final ExportJsonMojo mojo = new ExportJsonMojo();
    mojo.setTremaFile(tremaFilePath);
    mojo.setBasename("target/classes/test");
    mojo.setLanguages(new String[]{});
    mojo.setStates(new String[]{"verified"});

    // when
    mojo.execute();

    // then - no exception
  }

  @Test
  void shouldExecuteWithUnknownLanguage() throws Exception {
    // given
    final ExportJsonMojo mojo = new ExportJsonMojo();
    mojo.setTremaFile(tremaFilePath);
    mojo.setBasename("target/classes/test");
    mojo.setLanguages(new String[]{"foo"});
    mojo.setStates(new String[]{"verified"});

    // when
    mojo.execute();

    // then - no exception
  }

  /**
   * Test using the MessageFormat filter.
   *
   * @throws Exception if the test failed
   */
  @Test
  void testMessageFormatFilter() throws Exception {
    final ExportJsonMojo mojo = new ExportJsonMojo();
    mojo.setTremaFile(tremaFilePath);
    mojo.setBasename("target/classes/test");
    mojo.setLanguages(new String[]{"foo"});
    mojo.setStates(new String[]{"verified"});
    mojo.setFilters(new String[]{"messageformat"});
    mojo.execute();
  }

  @Test
  void shouldThrowForUnknownFilter() {
    // given
    final ExportJsonMojo mojo = new ExportJsonMojo();
    mojo.setTremaFile("src/test/resources/text.trm");
    mojo.setBasename("target/classes/test");
    mojo.setLanguages(new String[]{"foo"});
    mojo.setStates(new String[]{"verified"});
    mojo.setFilters(new String[]{"doesntexist"});

    // when
    MojoExecutionException ex = assertThrows(MojoExecutionException.class, mojo::execute);

    // then
    assertThat(ex.getMessage(), equalTo("Invalid Filter arguments: Invalid filter: doesntexist"));
  }

  /**
   * Test using a non-existing export filter.
   *
   * @throws Exception if the test failed
   */
  @Test
  void testDefaultLanguage() throws Exception {
    final ExportJsonMojo mojo = new ExportJsonMojo();
    mojo.setTremaFile(tremaFilePath);
    mojo.setLanguages(new String[]{});
    mojo.setBasename("target/classes/test");
    mojo.setDefaultlanguage("de");
    mojo.execute();
  }

}
