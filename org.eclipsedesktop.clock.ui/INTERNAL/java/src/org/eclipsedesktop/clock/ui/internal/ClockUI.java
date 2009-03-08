// Copyright (c) 2006 by Jordi Boehme Lopez and Leif Frenzel.
// All rights reserved.
package org.eclipsedesktop.clock.ui.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/** <p>The main plugin class for the clock UI.</p>
  *
  * @author The mighty PDE wizard
  */
public class ClockUI extends AbstractUIPlugin {

  // The shared instance
  private static ClockUI plugin;
  
  public ClockUI() {
    plugin = this;
  }
  
  public static ClockUI getDefault() {
    return plugin;
  }
  
  public static String getPluginId() {
    return getDefault().getBundle().getSymbolicName();
  }
}
