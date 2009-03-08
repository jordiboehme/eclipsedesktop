//Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.cpumon;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class CpumonPlugin extends AbstractUIPlugin {

  private static CpumonPlugin plugin;
  private ResourceBundle resourceBundle;

  public CpumonPlugin() {
    super();
    plugin = this;
  }

  public void start( final BundleContext context ) throws Exception {
    super.start( context );
  }

  public void stop( final BundleContext context ) throws Exception {
    super.stop( context );
    plugin = null;
    resourceBundle = null;
  }

  public static CpumonPlugin getDefault() {
    return plugin;
  }

  public static String getResourceString( final String key ) {
    String result = "";
    ResourceBundle bundle = CpumonPlugin.getDefault().getResourceBundle();
    try {
      result = ( bundle != null )
                               ? bundle.getString( key )
                               : key;
    } catch( MissingResourceException e ) {
      result = key;
    }
    return result;
  }

  public ResourceBundle getResourceBundle() {
    try {
      if( resourceBundle == null )
        resourceBundle = ResourceBundle.getBundle( "org.eclipsedesktop.cpumon.CpumonPluginResources" );
    } catch( MissingResourceException x ) {
      resourceBundle = null;
    }
    return resourceBundle;
  }
}
