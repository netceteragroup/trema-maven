package com.netcetera.trema.maven;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.File;

/**
 * Test utilities.
 */
public final class TestUtils {

  private TestUtils() {
  }

  /**
   * Returns a matcher that evaluates successfully if the evaluated file exists.
   *
   * @return file exists matcher
   */
  public static Matcher<File> isExistingFile() {
    return new TypeSafeMatcher<File>() {

      @Override
      public void describeTo(Description description) {
        description.appendText("An existing file");
      }

      @Override
      protected boolean matchesSafely(File item) {
        return item.exists();
      }

      @Override
      protected void describeMismatchSafely(File item, Description mismatchDescription) {
        mismatchDescription.appendText("File does not exist: " + item);
      }
    };
  }
}
