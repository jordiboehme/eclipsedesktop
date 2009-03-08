// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.monitorcenter;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The main plugin class to be used in the desktop.
 */
public class MonitorCenterPlugin extends AbstractUIPlugin {

  private static MonitorCenterPlugin plugin;

  public MonitorCenterPlugin() {
    super();
    plugin = this;
  }

  public static MonitorCenterPlugin getDefault() {
    return plugin;
  }
}