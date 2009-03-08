// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.eclipsesetimon;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * <p>
 * The main plugin class for the EclipseSeti plugin.
 * </p>
 * 
 * @author The mighty PDE wizard
 */
public class EclipseSetiPlugin extends AbstractUIPlugin {

  //The shared instance.
  private static EclipseSetiPlugin plugin;

  public EclipseSetiPlugin() {
    super();
    plugin = this;
  }

  public static EclipseSetiPlugin getDefault() {
    return plugin;
  }

  public static String getPluginId() {
    return getDefault().getBundle().getSymbolicName();
  }
}