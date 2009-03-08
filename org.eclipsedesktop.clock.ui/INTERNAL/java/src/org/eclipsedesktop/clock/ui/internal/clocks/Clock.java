// Copyright (c) 2006 by Jordi Boehme Lopez and Leif Frenzel.
// All rights reserved.
package org.eclipsedesktop.clock.ui.internal.clocks;

import org.eclipsedesktop.clock.ui.clocks.IClockStyle;


/** <p>implementation of the <code>IClock</code> interface, used by the 
  * manager to load clock extensions.</p>
  *
  * @author Leif Frenzel
  */
class Clock implements IClock {

  private final IClockStyle clockStyle;
  private final String name;
  private final String id;

  Clock( final String id, final String name, final IClockStyle clockStyle ) {
    this.id = id;
    this.name = name;
    this.clockStyle = clockStyle;
  }
  
  
  // interface methods of IClock
  //////////////////////////////
  
  public IClockStyle getClockStyle() {
    return clockStyle;
  }
  
  public String getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
}
