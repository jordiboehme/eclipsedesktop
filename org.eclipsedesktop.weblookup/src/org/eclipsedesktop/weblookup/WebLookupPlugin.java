// Copyright (c) 2004 by Jordi Boehme Lopez (mail@jordi-boehme.de)
//
// See http://www.eclipsedesktop.org for more information.
package org.eclipsedesktop.weblookup;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The main plugin class to be used in the desktop.
 */
public class WebLookupPlugin extends AbstractUIPlugin {

  public static final String EXTPOINT_LOOKUPS 
    = "org.eclipsedesktop.weblookup.lookups";
  private static WebLookupPlugin plugin;

  public WebLookupPlugin() {
    super();
    plugin = this;
  }

  public static WebLookupPlugin getDefault() {
    return plugin;
  }
}