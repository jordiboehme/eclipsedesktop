// Copyright (c) 2006 by Jordi Boehme Lopez and Leif Frenzel.
// All rights reserved.
package org.eclipsedesktop.clock.core.internal.alarm;

import java.util.Date;

/** <p>an <code>IAlarm</code> represents a time at which something is 
  * supposed to happen that shows the user that the alarm has been 
  * triggered.</p> */
public interface IAlarm {

  void setActive( boolean active );
  boolean isActive();
  void setTime( Date time );
  Date getTime();
  void setDescription( String description );
  String getDescription();
}
