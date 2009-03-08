// Copyright (c) 2006 by Jordi Boehme Lopez and Leif Frenzel.
// All rights reserved.
package org.eclipsedesktop.clock.core.internal.alarm;

import java.util.ArrayList;
import java.util.List;

/** <p>a singleton that manages all current alarms.</p>
  *
  * @author Leif Frenzel
  */
public class AlarmsManager {

  private static AlarmsManager _instance;
  
  private List<IAlarm> alarms;
  
  private AlarmsManager() {
    alarms = new ArrayList<IAlarm>();
  }
  
  public static synchronized AlarmsManager getInstance() {
    if( _instance == null ) {
      _instance = new AlarmsManager();
    }
    return _instance;
  }
  
  public IAlarm[] getAll() {
    return alarms.toArray( new IAlarm[ alarms.size() ] );
  }
  
  public void add( final IAlarm alarm ) {
    alarms.add( alarm );
  }
  
  public void remove( final IAlarm alarm ) {
    alarms.remove( alarm );
  }
}
