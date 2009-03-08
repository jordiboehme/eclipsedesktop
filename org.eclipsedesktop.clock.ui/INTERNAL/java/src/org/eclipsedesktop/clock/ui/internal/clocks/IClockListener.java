// Copyright (c) 2006 by Jordi Boehme Lopez and Leif Frenzel.
// All rights reserved.
package org.eclipsedesktop.clock.ui.internal.clocks;


/** <p>implementations can register with the {@link ClockManager ClockManager}
  * in order to get notified if the currently selected clock is changed.</p>
  *
  * @author Leif Frenzel
  */
public interface IClockListener {

  void clockChanged();
}
