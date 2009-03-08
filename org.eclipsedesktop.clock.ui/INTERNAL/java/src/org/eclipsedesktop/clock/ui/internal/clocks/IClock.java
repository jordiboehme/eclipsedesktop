// Copyright (c) 2006 by Jordi Boehme Lopez and Leif Frenzel.
// All rights reserved.
package org.eclipsedesktop.clock.ui.internal.clocks;

import org.eclipsedesktop.clock.ui.clocks.IClockStyle;


/** <p>implementations of this interface represent different clocks, like
  * digital, analog, binary etc. clocks. There is an <code>IClock</code>
  * object managed by the {@link ClockManager ClockStyleManager} for
  * each extension to the extension point 
  * <code>org.eclipsedesktop.clock.ui.clocks</code>.</p> */
public interface IClock {

  /** <p>returns a human-readable name for this <code>IClock</code> as 
    * specified in the <code>plugin.xml</code>. This should be used for
    * display to the user in the UI.</p> */
  String getName();
  /** <p>returns the unique identifier of this <code>IClock</code>, as
    * specified in the <code>plugin.xml</code>.</p> */
  String getId();
  IClockStyle getClockStyle();
  
}
