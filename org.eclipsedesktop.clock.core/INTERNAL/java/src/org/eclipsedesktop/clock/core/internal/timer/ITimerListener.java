// Copyright (c) 2006 by Jordi Boehme Lopez and Leif Frenzel.
// All rights reserved.
package org.eclipsedesktop.clock.core.internal.timer;

/** <p>implementation can register with a <code>Timer</code> job and get 
  * notified regularly.</p> 
  * 
  * @author Leif Frenzel
  */
public interface ITimerListener {

  void secondChanged();
  void minuteChanged();
}
