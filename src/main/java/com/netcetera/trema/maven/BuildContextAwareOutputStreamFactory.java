/**
 *
 */
package com.netcetera.trema.maven;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.sonatype.plexus.build.incremental.BuildContext;

import com.netcetera.trema.core.exporting.OutputStreamFactory;


/**
 * Creates an output stream using a
 * {@link org.sonatype.plexus.build.incremental.BuildContext}. When invoked
 * through m2eclipse, the EclipseBuildContext is used which allows
 * eclipse/m2eclipse to monitor files that are being created by the plugin
 * execution . When invoked through standard maven, a DefaultBuildContext is
 * used.
 *
 * See <a href="https://wiki.eclipse.org/M2E_compatible_maven_plugins">
 * https://wiki.eclipse.org/M2E_compatible_maven_plugins</a>
 *
 * @author tzueblin
 *
 */
public class BuildContextAwareOutputStreamFactory implements OutputStreamFactory {


  private BuildContext buildContext;


  /**
   * Constructor.
   *
   * @param buildContext the buildContext
   */
  public BuildContextAwareOutputStreamFactory(BuildContext buildContext) {
    this.buildContext = buildContext;
  }

  /*
   * (non-Jsdoc)
   * @see
   * com.netcetera.trema.console.OutputStreamFactory#createOutputStream(java.
   * io.File)
   */
  @Override
  public OutputStream createOutputStream(File file) throws IOException {
    return buildContext.newFileOutputStream(file);
  }

}
