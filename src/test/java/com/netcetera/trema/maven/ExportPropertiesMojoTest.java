package com.netcetera.trema.maven;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Assert;
import org.junit.Test;

import static com.netcetera.trema.maven.ExportMojoTestUtils.BASENAME;
import static com.netcetera.trema.maven.ExportMojoTestUtils.TREMA_FILE;

/**
 * Unit test for the property file export mojo ({@link ExportPropertiesMojo}).
 */
public class ExportPropertiesMojoTest {

  private static final String PROPERTIES_EXTENSION = ".properties";

  private ExportMojoTestUtils exportMojoTestUtils = new ExportMojoTestUtils(PROPERTIES_EXTENSION);

  /**
   * Test method for {@link com.netcetera.trema.maven.ExportPropertiesMojo#execute()}.
   *
   * @throws Exception if the test failed
   */
  @Test
  public void testExecute() throws Exception {
    exportMojoTestUtils.executeExportPropertiesMojo(new String[]{"en", "de", "fr"}, new String[]{"verified"});

    // make sure the files where written
    Assert.assertTrue(new File("target/classes/test_de.properties").exists());
    Assert.assertTrue(new File("target/classes/test_en.properties").exists());
    Assert.assertTrue(new File("target/classes/test_fr.properties").exists());
  }

  @Test
  public void executeExportPropertiesMojoReturnsReadableResourceBundle() {
    exportMojoTestUtils.executeExportPropertiesMojo(new String[]{"de", "en"}, new String[]{"verified", "translated"});

    ResourceBundle bundle = ResourceBundle.getBundle("test", Locale.GERMANY);
    Assert.assertEquals("Test (de)", bundle.getString("test.simple"));
    Assert.assertEquals("ÄöÜ[@", bundle.getString("test.umlaute"));

     bundle = ResourceBundle.getBundle("test", Locale.ENGLISH);
    Assert.assertEquals("Test (en)", bundle.getString("test.simple"));
    Assert.assertEquals("ÀÉÈÔô", bundle.getString("test.umlaute"));
  }

  /**
   * Test with no trema file specification.
   *
   * @throws Exception if the test failed
   */
  @Test(expected = MojoExecutionException.class)
  public void testNoTremaFile() throws Exception {
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
    mojo.setBasename(BASENAME);
    mojo.setLanguages(new String[]{"en", "de", "fr"});
    mojo.setStates(new String[]{"verified"});
    mojo.execute();
  }

  /**
   * Test with an non-existent trema file.
   *
   * @throws Exception if the test failed
   */
  @Test(expected = MojoExecutionException.class)
  public void testNonExistentTremaFile() throws Exception {
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
    mojo.setTremaFile("src/test/resources/text-nonesistent.trm");
    mojo.setBasename(BASENAME);
    mojo.setLanguages(new String[]{"en", "de", "fr"});
    mojo.setStates(new String[]{"verified"});
    mojo.execute();
  }

  /**
   * Test with no basename.
   *
   * @throws Exception if the test failed
   */
  @Test(expected = MojoExecutionException.class)
  public void testNoBasename() throws Exception {
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
    mojo.setTremaFile(TREMA_FILE);
    mojo.setLanguages(new String[]{"en", "de", "fr"});
    mojo.setStates(new String[]{"verified"});
    mojo.execute();
  }

  /**
   * Test with no languages.
   *
   * @throws Exception if the test failed
   */
  @Test
  public void testNoLanguages() throws Exception {
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
  public void testEmptyLanguages() throws Exception {
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
  public void testInvalidLanguages() throws Exception {
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
  public void testMessageFormatFilter() throws Exception {
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
    mojo.setTremaFile(TREMA_FILE);
    mojo.setBasename(BASENAME);
    mojo.setLanguages(new String[]{"foo"});
    mojo.setStates(new String[]{"verified"});
    mojo.setFilters(new String[]{"messageformat"});
    mojo.execute();
  }

  /**
   * Test using a non-existing export filter.
   *
   * @throws Exception if the test failed
   */
  @Test(expected = MojoExecutionException.class)
  public void testNonExistingFilter() throws Exception {
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
    mojo.setTremaFile(TREMA_FILE);
    mojo.setBasename(BASENAME);
    mojo.setLanguages(new String[]{"foo"});
    mojo.setStates(new String[]{"verified"});
    mojo.setFilters(new String[]{"doesntexist"});
    mojo.execute();
  }

  /**
   * Test using a non-existing export filter.
   *
   * @throws Exception if the test failed
   */
  @Test
  public void testDefaultLanguage() throws Exception {
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
    mojo.setTremaFile(TREMA_FILE);
    mojo.setBasename(BASENAME);
    mojo.setLanguages(new String[]{});
    mojo.setDefaultlanguage("de");
    mojo.execute();
  }



}
