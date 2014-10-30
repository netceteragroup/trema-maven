package com.netcetera.trema.maven;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link ExportAndroidMojo}.
 */
public class ExportAndroidMojoTest {

  /**
   * Trema file location used in all test cases where valid trema file is
   * required.
   */
  private static final String TREMA_FILE = "src/test/resources/text.trm";

  /**
   * Path for export files.
   */
  private static final String EXPORT_PATH = "target/res/";

  /**
   * Test method for
   * {@link com.netcetera.trema.maven.ExportAndroidMojo#execute()}.
   *
   * @throws Exception if the test failed
   */
  @Test
  public void testExecute() throws Exception {

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
    mojo.execute();

    // make sure the files where written
    Assert.assertTrue(new File(file1).exists());
    Assert.assertTrue(new File(file2).exists());
    Assert.assertTrue(new File(file3).exists());
  }

  /**
   * Test with no trema file specification.
   *
   * @throws Exception if the test failed
   */
  @Test(expected = MojoExecutionException.class)
  public void testNoTremaFile() throws Exception {
    final ExportAndroidMojo mojo = new ExportAndroidMojo();
    mojo.setExportPath("target/classes/test");
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
    final ExportAndroidMojo mojo = new ExportAndroidMojo();
    mojo.setTremaFile("src/test/resources/text-nonesistent.trm");
    mojo.setExportPath("target/classes/test");
    mojo.setLanguages(new String[]{"en", "de", "fr"});
    mojo.setStates(new String[]{"verified"});
    mojo.execute();
  }

  /**
   * Test with no EXPORT_PATH.
   *
   * @throws Exception if the test failed
   */
  @Test(expected = MojoExecutionException.class)
  public void testNoExportPath() throws Exception {
    final ExportAndroidMojo mojo = new ExportAndroidMojo();
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
    final ExportAndroidMojo mojo = new ExportAndroidMojo();
    mojo.setTremaFile(TREMA_FILE);
    mojo.setExportPath("target/classes/test");
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
    final ExportAndroidMojo mojo = new ExportAndroidMojo();
    mojo.setTremaFile(TREMA_FILE);
    mojo.setExportPath("target/classes/test");
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
    final ExportAndroidMojo mojo = new ExportAndroidMojo();
    mojo.setTremaFile(TREMA_FILE);
    mojo.setExportPath("target/classes/test");
    mojo.setLanguages(new String[]{"foo"});
    mojo.setStates(new String[]{"verified"});
    mojo.execute();
  }

  /**
   * Test using a non-existing export filter.
   *
   * @throws Exception if the test failed
   */
  @Test
  public void testDefaultLanguage() throws Exception {
    String file1 = EXPORT_PATH + "values/strings.xml";
    String file2 = EXPORT_PATH + "values-de/strings.xml";
    String file3 = EXPORT_PATH + "values-en/strings.xml";

    // delete the target files if they exist
    new File(file1).delete();
    new File(file2).delete();
    new File(file3).delete();

    final ExportAndroidMojo mojo = new ExportAndroidMojo();
    mojo.setTremaFile(TREMA_FILE);
    mojo.setExportPath(EXPORT_PATH);
    mojo.setDefaultlanguage("en");
    mojo.execute();

    // make sure the files where written
    Assert.assertTrue(new File(file1).exists());
    Assert.assertTrue(new File(file2).exists());
    Assert.assertFalse(new File(file3).exists());
  }

}
