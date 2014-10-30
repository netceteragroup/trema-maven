package com.netcetera.trema.maven;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for the property file export mojo ({@link ExportPropertiesMojo}).
 */
public class ExportPropertiesMojoTest {


  /**
   * Test method for
   * {@link com.netcetera.trema.maven.ExportPropertiesMojo#execute()}.
   *
   * @throws Exception if the test failed
   */
  @Test
  public void testExecute() throws Exception {

    // delete the target files if they exist
    new File("target/classes/test_de.properties").delete();
    new File("target/classes/test_en.properties").delete();
    new File("target/classes/test_fr.properties").delete();

    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
    mojo.setTremaFile("src/test/resources/text.trm");
    mojo.setBasename("target/classes/test");
    mojo.setLanguages(new String[]{"en", "de", "fr"});
    mojo.setStates(new String[]{"verified"});
    mojo.execute();

    // make sure the files where written
    Assert.assertTrue(new File("target/classes/test_de.properties").exists());
    Assert.assertTrue(new File("target/classes/test_en.properties").exists());
    Assert.assertTrue(new File("target/classes/test_fr.properties").exists());

  }

  /**
   * Test with no trema file specification.
   *
   * @throws Exception if the test failed
   */
  @Test(expected = MojoExecutionException.class)
  public void testNoTremaFile() throws Exception {
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
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
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
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
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
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
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
    mojo.setTremaFile("src/test/resources/text.trm");
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
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
    mojo.setTremaFile("src/test/resources/text.trm");
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
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
    mojo.setTremaFile("src/test/resources/text.trm");
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
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
    mojo.setTremaFile("src/test/resources/text.trm");
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
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
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
    final ExportPropertiesMojo mojo = new ExportPropertiesMojo();
    mojo.setTremaFile("src/test/resources/text.trm");
    mojo.setLanguages(new String[]{});
    mojo.setBasename("target/classes/test");
    mojo.setDefaultlanguage("de");
    mojo.execute();
  }

}
