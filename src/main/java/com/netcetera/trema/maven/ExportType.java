package com.netcetera.trema.maven;


/**
 * Type of export.
 */
public enum ExportType {
  /** comma separated value files. */
  CSV("CSV"),

  /** .properties files. */
  PROPERTIES("properties"),

  /** XLS. */
  XLS("excel"),

  /** XML files for android. */
  ANDROID("android xml"),

  /** JSON files for AngularJS. */
  JSON("JSON");

  private String name;
  private ExportType(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}
