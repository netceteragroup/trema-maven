package com.netcetera.trema.maven;

import java.io.File;
import java.net.URL;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for the property file export mojo ({@link com.netcetera.trema.maven.ExportPropertiesMojo}).
 */
public class ExportJsonMojoTest {

  private String tremaFilePath;
  @Before
  public void setup() {
    URL url = getClass().getClassLoader().getResource("text.trm");
    tremaFilePath =  url.getFile();
  }


  /**
   * Test method for
   * {@link ExportJsonMojo#execute()}.
   *
   * @throws Exception if the test failed
   */
  @Test
  public void testExecute() throws Exception {

    // delete the target files if they exist
    new File("target/classes/test_de.json").delete();
    new File("target/classes/test_en.json").delete();
    new File("target/classes/test_fr.json").delete();

    final ExportJsonMojo mojo = new ExportJsonMojo();
    mojo.setTremaFile(tremaFilePath);
    mojo.setBasename("target/classes/test");
    mojo.setLanguages(new String[]{"en", "de", "fr"});
    mojo.setStates(new String[]{"verified"});
    mojo.execute();

    // make sure the files where written
    Assert.assertTrue(new File("target/classes/test_de.json").exists());
    Assert.assertTrue(new File("target/classes/test_en.json").exists());
    Assert.assertTrue(new File("target/classes/test_fr.json").exists());

  }

  /**
   * Test with no trema file specification.
   *
   * @throws Exception if the test failed
   */
  @Test(expected = MojoExecutionException.class)
  public void testNoTremaFile() throws Exception {
    final ExportJsonMojo mojo = new ExportJsonMojo();
    mojo.setBasename("target/classes/test");
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
    final ExportJsonMojo mojo = new ExportJsonMojo();
    mojo.setTremaFile("src/test/resources/text-nonesistent.trm");
    mojo.setBasename("target/classes/test");
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
    final ExportJsonMojo mojo = new ExportJsonMojo();
    mojo.setTremaFile("src/test/resources/text.trm");
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
    final ExportJsonMojo mojo = new ExportJsonMojo();
    mojo.setTremaFile(tremaFilePath);
    mojo.setBasename("target/classes/test");
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
    final ExportJsonMojo mojo = new ExportJsonMojo();
    mojo.setTremaFile(tremaFilePath);
    mojo.setBasename("target/classes/test");
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
    final ExportJsonMojo mojo = new ExportJsonMojo();
    mojo.setTremaFile(tremaFilePath);
    mojo.setBasename("target/classes/test");
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
    final ExportJsonMojo mojo = new ExportJsonMojo();
    mojo.setTremaFile(tremaFilePath);
    mojo.setBasename("target/classes/test");
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
    final ExportJsonMojo mojo = new ExportJsonMojo();
    mojo.setTremaFile("src/test/resources/text.trm");
    mojo.setBasename("target/classes/test");
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
    final ExportJsonMojo mojo = new ExportJsonMojo();
    mojo.setTremaFile(tremaFilePath);
    mojo.setLanguages(new String[]{});
    mojo.setBasename("target/classes/test");
    mojo.setDefaultlanguage("de");
    mojo.execute();
  }

}
