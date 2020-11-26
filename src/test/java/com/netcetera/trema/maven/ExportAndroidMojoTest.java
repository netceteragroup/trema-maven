package com.netcetera.trema.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.jupiter.api.Test;

import java.io.File;

import static com.netcetera.trema.maven.TestUtils.isExistingFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test for {@link ExportAndroidMojo}.
 */
class ExportAndroidMojoTest {

  /**
   * Trema file location used in all test cases where valid trema file is required.
   */
  private static final String TREMA_FILE = "src/test/resources/text.trm";

  /**
   * Path for export files.
   */
  private static final String EXPORT_PATH = "target/res/";

  @Test
  void shouldExecuteSuccessfully() throws Exception {
    // given
    String file1 = EXPORT_PATH + "values-de/strings.xml";
    String file2 = EXPORT_PATH + "values-en/strings.xml";
    String file3 = EXPORT_PATH + "values-fr/strings.xml";

    // delete the target files if they exist
    new File(file1).delete();
    new File(file2).delete();
    new File(file3).delete();

    final ExportAndroidMojo mojo = new ExportAndroidMojo();
    mojo.setTremaFile(TREMA_FILE);
    mojo.setExportPath(EXPORT_PATH);
    mojo.setLanguages(new String[]{"en", "de", "fr"});
    mojo.setStates(new String[]{"verified"});

    // when
    mojo.execute();

    // then
    // make sure the files where written
    assertThat(new File(file1), isExistingFile());
    assertThat(new File(file2), isExistingFile());
    assertThat(new File(file3), isExistingFile());
  }

  @Test
  void shouldThrowForMissingTremaFile() {
    // given
    final ExportAndroidMojo mojo = new ExportAndroidMojo();
    mojo.setExportPath("target/classes/test");
    mojo.setLanguages(new String[]{"en", "de", "fr"});
    mojo.setStates(new String[]{"verified"});

    // when
    MojoExecutionException ex = assertThrows(MojoExecutionException.class, mojo::execute);

    // then
    assertThat(ex.getMessage(), equalTo("tremaFile must not be empty"));
  }

  @Test
  void shouldThrowForNonExistentFile() {
    // given
    final ExportAndroidMojo mojo = new ExportAndroidMojo();
    mojo.setTremaFile("src/test/resources/text-nonesistent.trm");
    mojo.setExportPath("target/classes/test");
    mojo.setLanguages(new String[]{"en", "de", "fr"});
    mojo.setStates(new String[]{"verified"});

    // when / then
    assertThrows(MojoExecutionException.class, mojo::execute);
  }

  @Test
  void shouldThrowForMissingBasename() {
    // given
    final ExportAndroidMojo mojo = new ExportAndroidMojo();
    mojo.setTremaFile(TREMA_FILE);
    mojo.setLanguages(new String[]{"en", "de", "fr"});
    mojo.setStates(new String[]{"verified"});

    // when
    MojoExecutionException ex = assertThrows(MojoExecutionException.class, mojo::execute);

    // then
    assertThat(ex.getMessage(), equalTo("basename must not be empty"));
  }

  @Test
  void shouldPerformExportWithNullLanguageSet() throws Exception {
    // given
    final ExportAndroidMojo mojo = new ExportAndroidMojo();
    mojo.setTremaFile(TREMA_FILE);
    mojo.setExportPath("target/classes/test");
    mojo.setStates(new String[]{"verified"});

    // when
    mojo.execute();

    // then - no exception
  }

  /**
   * Test with empty language configuration.
   *
   * @throws Exception if the test failed
   */
  @Test
  void shouldPerformExportWithEmptyLanguageSet() throws Exception {
    // given
    final ExportAndroidMojo mojo = new ExportAndroidMojo();
    mojo.setTremaFile(TREMA_FILE);
    mojo.setExportPath("target/classes/test");
    mojo.setLanguages(new String[]{});
    mojo.setStates(new String[]{"verified"});

    // when
    mojo.execute();

    // then - no exception
  }

  @Test
  void shouldExecuteWithUnknownLanguage() throws Exception {
    // given
    final ExportAndroidMojo mojo = new ExportAndroidMojo();
    mojo.setTremaFile(TREMA_FILE);
    mojo.setExportPath("target/classes/test");
    mojo.setLanguages(new String[]{"foo"});
    mojo.setStates(new String[]{"verified"});

    // when
    mojo.execute();

    // then - no exception
  }

  @Test
  void shouldExportWithDefaultLanguage() throws Exception {
    // given
    String file1 = EXPORT_PATH + "values/strings.xml";
    String file2 = EXPORT_PATH + "values-de/strings.xml";
    String file3 = EXPORT_PATH + "values-en/strings.xml";

    // delete the target files if they exist
    new File(file1).delete();
    new File(file2).delete();
    new File(file3).delete();

    // when
    final ExportAndroidMojo mojo = new ExportAndroidMojo();
    mojo.setTremaFile(TREMA_FILE);
    mojo.setExportPath(EXPORT_PATH);
    mojo.setDefaultlanguage("en");
    mojo.execute();

    // then
    // make sure the files where written
    assertThat(new File(file1), isExistingFile());
    assertThat(new File(file2), isExistingFile());
    assertThat(new File(file3), not(isExistingFile()));
  }
}
