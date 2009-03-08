package org.eclipsedesktop.base;

import org.eclipse.ui.plugin.*;
import org.osgi.framework.BundleContext;
import java.util.*;

/**
 * The main plugin class to be used in the desktop.
 */
public class EclipsedesktopPlugin extends AbstractUIPlugin {

  //The shared instance.
  private static EclipsedesktopPlugin plugin;
  //Resource bundle.
  private ResourceBundle resourceBundle;

  /**
   * The constructor.
   */
  public EclipsedesktopPlugin() {
    super();
    plugin = this;
    try {
      String bundle = "org.eclipsedesktop.base.EclipsedesktopCorePluginResources";
      resourceBundle = ResourceBundle.getBundle( bundle );
    } catch( MissingResourceException x ) {
      resourceBundle = null;
    }
  }

  /**
   * This method is called upon plug-in activation
   */
  public void start( final BundleContext context ) throws Exception {
    super.start( context );
  }

  /**
   * This method is called when the plug-in is stopped
   */
  public void stop( final BundleContext context ) throws Exception {
    super.stop( context );
  }

  /**
   * Returns the shared instance.
   */
  public static EclipsedesktopPlugin getDefault() {
    return plugin;
  }

  /**
   * Returns the string from the plugin's resource bundle, or 'key' if not
   * found.
   */
  public static String getResourceString( final String key ) {
    String result = "";
    ResourceBundle bundle = EclipsedesktopPlugin.getDefault()
      .getResourceBundle();
    try {
      result = ( bundle != null )
                               ? bundle.getString( key )
                               : key;
    } catch( MissingResourceException e ) {
      result = key;
    }
    return result;
  }

  /**
   * Returns the plugin's resource bundle,
   */
  public ResourceBundle getResourceBundle() {
    return resourceBundle;
  }
}