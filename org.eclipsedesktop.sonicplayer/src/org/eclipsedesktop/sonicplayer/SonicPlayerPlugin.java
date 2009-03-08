// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.sonicplayer;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipsedesktop.sonicplayer.core.player.ISonicPlayer;
import org.osgi.framework.BundleContext;

/** <p>TODO</p>
 * 
 * @author Jordi Böhme López (mail@jordi-boehme.de)
 */
public class SonicPlayerPlugin extends AbstractUIPlugin {

  private static SonicPlayerPlugin plugin;
  private ResourceBundle resourceBundle;
  private ISonicPlayer player = null;

  public SonicPlayerPlugin() {
    super();
    plugin = this;
    try {
      resourceBundle = ResourceBundle.getBundle( "org.eclipsedesktop.base.sonicplayer.ui.UiPluginResources" );
    } catch( MissingResourceException x ) {
      resourceBundle = null;
    }
  }

  public void start( final BundleContext context ) throws Exception {
    super.start( context );
  }

  public void stop( final BundleContext context ) throws Exception {
    super.stop( context );
  }

  public static SonicPlayerPlugin getDefault() {
    return plugin;
  }

  public static String getResourceString( final String key ) {
    String result = "";
    ResourceBundle bundle = SonicPlayerPlugin.getDefault().getResourceBundle();
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
    return resourceBundle;
  }
  
  public ISonicPlayer getActivePlayer() {
    return plugin.player;
  }

  public void setActivePlayer( final ISonicPlayer sonicPlayer ) {
    plugin.player = sonicPlayer;
  }
}