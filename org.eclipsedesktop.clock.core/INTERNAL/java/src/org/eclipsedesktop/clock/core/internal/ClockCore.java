// Copyright (c) 2006 by Jordi Boehme Lopez and Leif Frenzel.
// All rights reserved.
package org.eclipsedesktop.clock.core.internal;

import org.eclipse.core.runtime.Plugin;

/** <p>The main plugin class for the Clock core.</p>
  * 
  * @author The mighty PDE wizard
  */
public class ClockCore extends Plugin {

  // The shared instance
  private static ClockCore plugin;

  public ClockCore() {
    plugin = this;
  }

  public static ClockCore getDefault() {
    return plugin;
  }
}
