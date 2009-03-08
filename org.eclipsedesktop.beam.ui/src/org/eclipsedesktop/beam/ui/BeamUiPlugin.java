package org.eclipsedesktop.beam.ui;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipsedesktop.beam.ui.preferences.BeamPreferences;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class BeamUiPlugin extends AbstractUIPlugin {

  public static final String BEAMLIST_FILENAME = "beamlist.xml";
  // The shared instance.
  private static BeamUiPlugin plugin;

  /**
   * The constructor.
   */
  public BeamUiPlugin() {
    plugin = this;
  }

  /**
   * This method is called upon plug-in activation
   */
  public void start( BundleContext context ) throws Exception {
    super.start( context );
  }

  /**
   * This method is called when the plug-in is stopped
   */
  public void stop( BundleContext context ) throws Exception {
    super.stop( context );
    plugin = null;
  }

  /**
   * Returns the shared instance.
   */
  public static BeamUiPlugin getDefault() {
    return plugin;
  }

  /**
   * Returns an image descriptor for the image file at the given plug-in
   * relative path.
   * 
   * @param path the path
   * @return the image descriptor
   */
  public static ImageDescriptor getImageDescriptor( String path ) {
    return AbstractUIPlugin.imageDescriptorFromPlugin( "org.eclipsedesktop.beam.ui",
                                                       path );
  }

  protected void initializeDefaultPreferences( final IPreferenceStore store ) {
    store.setDefault( BeamPreferences.PEERNAME,
                      System.getProperty( "user.name" ) );
  }
}
