// Copyright (c) 2006 by Jordi Boehme Lopez and Leif Frenzel.
// All rights reserved.
package org.eclipsedesktop.clock.core.internal.timer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** <p>a singleton where clients can register to be notified about regular
  * timer events.</p> 
  *
  * @author Leif Frenzel
  */
public class Timer {

  private static Timer _instance;
  
  private List<ITimerListener> listeners;
  private String timeStamp = ""; //$NON-NLS-1$

  
  private Timer() {
    listeners = new ArrayList<ITimerListener>();
    new TimerJob().schedule();
  }

  void notifyListeners() {
    for( ITimerListener listener: listeners ) {
      listener.secondChanged();
    }
    
    Date date = new Date();
    SimpleDateFormat sdfH = new SimpleDateFormat( "hh:mm" ); //$NON-NLS-1$
    String newTimestamp = sdfH.format( date );
    
    if( !this.timeStamp .equals( newTimestamp ) ) {
      this.timeStamp = newTimestamp;
      for( ITimerListener listener: listeners ) {
        listener.minuteChanged();
      }
    }
  }

  
  // public API
  /////////////
  
  public static synchronized Timer getInstance() {
    if( _instance == null ) {
      _instance = new Timer();
    }
    return _instance;
  }
  
  public void addTimeListener( final ITimerListener listener ) {
    listeners.add( listener );
  }
}
