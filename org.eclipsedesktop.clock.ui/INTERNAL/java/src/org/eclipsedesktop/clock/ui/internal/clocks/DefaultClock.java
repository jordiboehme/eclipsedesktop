// Copyright (c) 2006 by Jordi Boehme Lopez and Leif Frenzel.
// All rights reserved.
package org.eclipsedesktop.clock.ui.internal.clocks;

import org.eclipsedesktop.clock.ui.clocks.IClockStyle;
import org.eclipsedesktop.clock.ui.internal.UITexts;



/** <p>an empty fail-safe implementation of the <code>IClock</code>
  * interface that is used if no clocks could be loaded from extensions.</p>
  *
  * @author Leif Frenzel
  */
class DefaultClock implements IClock{

  private IClockStyle defaultStyle = new DefaultClockStyle();
  
  
  // interface methods of IClock
  //////////////////////////////
  
  public IClockStyle getClockStyle() {
    return defaultStyle;
  }

  public String getId() {
    return DefaultClock.class.getName();
  }

  public String getName() {
    return UITexts.defaultClock_noClocks;
  }
}
